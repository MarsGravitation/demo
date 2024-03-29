package log

import (
	"bufio"
	"errors"
	"fmt"
	"io"
	"os"
	"path"
	"time"
)

// FileHandler writers log conf to a file
type FileHandler struct {
	fd *os.File
	name string
}

type RotatingFileHandler struct {
	fd *os.File

	fileName string
	maxBytes int
	curBytes int
	backupCount int
}

func NewRotatingFileHandler(filename string, maxBytes int, backupCount int) (*RotatingFileHandler, error){
	dir := path.Dir(filename)
	os.Mkdir(dir, 0777)

	h := new(RotatingFileHandler)
	
	if maxBytes <= 0 {
		return nil, fmt.Errorf("invalid max bytes")
	}
	
	h.fileName = filename
	h.maxBytes = maxBytes
	h.backupCount = backupCount
	
	var err error
	h.fd, err = os.OpenFile(filename, os.O_CREATE|os.O_RDWR|os.O_APPEND, 06666)
	if err != nil {
		return nil, err
	}

	f, err := h.fd.Stat()
	if err != nil {
		return nil, err
	}
	h.curBytes = int(f.Size())

	return h, nil
}


func (h *FileHandler) Name() string {
	return h.name
}

func (h *FileHandler) Write(b []byte) (n int, err error) {
	return h.fd.Write(b)
}

func (h *FileHandler) Close() error {
	return h.fd.Close()
}

func copyFile(src *os.File, dst *os.File) error {
	var buf = make([]byte, 4096)

	reader := bufio.NewReader(src)
	writer := bufio.NewWriter(dst)

	for {
		rn, err := reader.Read(buf)
		if err != nil {
			if err == io.EOF {
				break
			}
			return err
		}

		if rn == 0 {
			break
		}

		wn, err := writer.Write(buf[:rn])
		if err != nil {
			if err == io.EOF {
				break
			}
			return err
		}

		if rn != wn {
			return errors.New("read length not equal write length")
		}
	}

	writer.Flush()

	return nil
}

func (h *RotatingFileHandler) Name() string {
	return h.fileName
}

func (h *RotatingFileHandler) Write(p []byte) (n int, err error) {
	if err = h.doRollover(); err != nil {
		return n, err
	}
	//p = FileterColor(p)
	n, err = h.fd.Write(p)
	h.curBytes += n
	h.fd.Sync()
	return
}

func (h *RotatingFileHandler) Close() error {
	if h.fd != nil {
		return h.fd.Close()
	}
	return nil
}

func (h *RotatingFileHandler) doRollover() error {
	if h.curBytes < h.maxBytes {
		return nil
	}

	f, err := h.fd.Stat()
	if err != nil {
		return err
	}

	if h.maxBytes <= 0 {
		return err
	} else if f.Size() < int64(h.maxBytes) {
		h.curBytes = int(f.Size())
		return nil
	}

	switch {
	case h.backupCount == 0:
	// No restrictions
	case h.backupCount == 1:
		//Circular loop
		if h.curBytes > h.maxBytes {
			size := int64(float32(h.curBytes) * 0.4)
			stat, err := h.fd.Stat()
			if err == nil {
				size = stat.Size() / 2
			}
			if _, err := h.fd.Seek(size, os.SEEK_SET); err != nil {
				return err
			}

			new, err := os.OpenFile(h.fileName+".bak", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
			if err != nil {
				return err
			}

			if err = copyFile(h.fd, new); err != nil {
				return err
			}
			if err = h.fd.Close(); err != nil {
				return err
			}
			if err = new.Close(); err != nil {
				return err
			}
			// TODO may don't need the one line code.
			if err = os.Rename(h.fileName+".bak", h.fileName); err != nil {
				return err
			}

			new, err = os.OpenFile(h.fileName, os.O_CREATE|os.O_RDWR|os.O_APPEND, 0666)
			if err != nil {
				return err
			}
			h.fd = new

			h.curBytes = 0
			f, err := h.fd.Stat()
			if err != nil {
				return err
			}
			h.curBytes = int(f.Size())
		}
	case h.backupCount > 1:
		// File backup loop
		if h.curBytes > h.maxBytes {
			if err = h.fd.Close(); err != nil {
				return err
			}

			for i := h.backupCount - 2; i >= 0; i-- {
				sfn := h.fileName
				if i != 0 {
					sfn = fmt.Sprintf("%s.%d", h.fileName, i)
				}
				dfn := fmt.Sprintf("%s.%d", h.fileName, i + 1)
				os.Rename(sfn, dfn)
			}

			h.fd, err = os.OpenFile(h.fileName, os.O_CREATE|os.O_RDWR|os.O_APPEND, 0666)
			if err != nil {
				return err
			}

			h.curBytes = 0
			f, err := h.fd.Stat()
			if err != nil {
				return err
			}
			h.curBytes = int(f.Size())
		}
	}

	return nil
}

type TimeRotatingFileHandler struct {
	fd *os.File

	baseName string
	interval int64
	suffix string
	rolloverAt int64
}

const (
	WhenSecond = iota
	WhenMinute
	WhenHour
	WhenDay
)

func NewTimeRotatingFileHandler(baseName string, when int8, interval int) (*TimeRotatingFileHandler, error) {
	dir := path.Dir(baseName)
	os.Mkdir(dir, 0777)

	h := new(TimeRotatingFileHandler)

	h.baseName = baseName
	switch when {
	case WhenSecond:
		h.interval = 1
		h.suffix = "2006-01-02_15-04-05"
	case WhenMinute:
		h.interval = 60
		h.suffix = "2006-01-02_15-04"
	case WhenHour:
		h.interval = 3600
		h.suffix = "2006-01-02_15"
	case WhenDay:
		h.interval = 3600 * 24
		h.suffix = "2006-01-02"
	default:
		return nil, fmt.Errorf("invalid when_rotate: %d", when)
	}

	h.interval = h.interval * int64(interval)

	var err error
	h.fd, err = os.OpenFile(h.baseName, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		return nil, err
	}

	fInfo, _ := h.fd.Stat()
	h.rolloverAt = fInfo.ModTime().Unix() + h.interval

	return h, nil
}

func (h *TimeRotatingFileHandler) Name() string {
	return h.baseName
}

func (h *TimeRotatingFileHandler) doRollover() {
	now := time.Now()

	if h.rolloverAt <= now.Unix() {
		fName := h.baseName + now.Format(h.suffix)
		h.fd.Close()
		err := os.Rename(h.baseName, fName)
		if err != nil {
			panic(err)
		}

		h.fd, _ = os.OpenFile(h.baseName, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)

		h.rolloverAt = time.Now().Unix() + h.interval
	}
}

func (h *TimeRotatingFileHandler) Write(b []byte) (n int, err error) {
	h.doRollover()
	return h.fd.Write(b)
}

func (h *TimeRotatingFileHandler) Close() error {
	return h.fd.Close()
}