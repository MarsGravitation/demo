package log

import (
	"fmt"
	"os"
)

const defaultFileSize = "1024mb"
const defaultFileCount = 2

type file struct {
	Name string `json:"name"` // file name.
	Size string `json:"size"` // single file size.
	Count int `json:"count"` // files count.
}

type Config struct {
	Color bool `json:"color"` // false: only level name with color, true: then all with color.
	Level string `json:"level"` // logger level.
	Terminal bool `json:"terminal"` // output to terminal.
	File file `json:"file"` // output to file.
}

func NewConfig(color bool, level string, terminal bool) *Config {
	return &Config{
		Color: color,
		Level: level,
		Terminal: terminal,
		File: file{
			Name: "",
			Size: "",
			Count: 0,
		},
	}
}

func NewConfigFromLogger(log *Logger) *Config {
	color := log.color
	level := "INFO"
	var f file

	switch log.level {
	case TRACE:
		level = "TRACE"
	case DEBUG:
		level = "DEBUG"
	case WARNING:
		level = "WARNING"
	case INFO:
		level = "INFO"
	case ERROR:
		level = "ERROR"
	case FATAL:
		level = "FATAL"
	default:
		level = "INFO"
	}

	if len(log.out) > 0 {
		f.Name = log.out[0].Name()
		f.Size = defaultFileSize
		f.Count = defaultFileCount
	}

	return &Config{
		Color: color,
		Level: level,
		Terminal: log.terminal,
		File: f,
	}
}

func (c *Config) ToLogger(log *Logger) {
	color := c.Color
	level := INFO

	switch {
	case c.Level == "trace" || c.Level == "TRACE" || c.Level == "Trace":
		level = TRACE
	case c.Level == "debug" || c.Level == "DEBUG" || c.Level == "Debug":
		level = DEBUG
	case c.Level == "info" || c.Level == "INFO" || c.Level == "Info":
		level = INFO
	case c.Level == "warning" || c.Level == "WARNING" || c.Level == "Warning":
		level = WARNING
	case c.Level == "error" || c.Level == "ERROR" || c.Level == "Error":
		level = ERROR
	case c.Level == "fatal" || c.Level == "FATAL" || c.Level == "Fatal":
		level = FATAL
	default:
		level = INFO
	}

	var out []Writer
	var f = c.File
	if f.Name != "" && f.Size != "" && f.Count != 0 {
		var size int
		var unit string
		fmt.Sscanf(f.Size, "%d%s", &size, &unit)

		switch {
		case unit == "b" || unit == "B" || unit == "":
		case unit == "k" || unit == "K" || unit == "kb" || unit == "KB":
			size *= 1024
		case unit == "m" || unit == "M" || unit == "mb" || unit == "MB":
			size *= 1024 * 1024
		case unit == "g" || unit == "G" || unit == "gb" || unit == "GB":
			size *= 1024 * 1024 * 1024
		}

		fh, err := NewRotatingFileHandler(f.Name, size, f.Count)
		if err == nil {
			out = append(out, fh)
		}
	}

	log.mutex.Lock()
	defer log.mutex.Unlock()

	if log.lock != nil {
		log.lock.Lock()
		defer log.lock.Unlock()
	}

	for _, out := range log.out {
		if out != os.Stdout && out != os.Stderr && out != os.Stdin {
			out.Close()
		}
	}

	log.terminal = c.Terminal
	log.out = out
	log.color = color
	log.level = level
}
