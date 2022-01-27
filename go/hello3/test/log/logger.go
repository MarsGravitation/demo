package log

import (
	"fmt"
	"io"
	"os"
	"runtime"
	"sync"
	"time"
)

type formatCB func(level int, fmt string, v... interface{}) string

type Writer interface {
	Name() string
	Write(p []byte) (n int, err error)
	Close() error
}

// Lock cross-process locks to ensure complete writes.
type Lock interface {
	Lock()
	Unlock()
}

type logEntry struct {
	writer Writer
	color int
	format string
	v []interface{}
}

type queue struct {
	enable bool
	isStop chan bool
	buf chan logEntry
}

type Logger struct {
	level int // logger level.
	out []Writer // destination for output.
	mutex sync.Mutex // ensures atomic writes; protects the following fields.
	lock Lock // cross-process locks to ensure complete writes.
	format formatCB // user format string callback.
	color bool // false: only level name with color; true: then all with color.
	terminal bool // output in the terminal.
	queue queue // buffer queue, support asynchronous processing.
}

const (
	ALL = iota
	TRACE
	DEBUG
	WARNING
	INFO
	ERROR
	FATAL
	OFF
)

const (
	Black = 0x000000AA << iota
	Blue
	Green
	Cyan
	Red
	Purple
	Yellow
	White

	Gray
	LightBlue
	LightGreen
	LightCyan
	LightRed
	LightPurple
	LightYellow
	LightWhite
	DefaultColor
)

var levelName = map[int]string {
	TRACE: "TRACE",
	DEBUG: "DEBUG",
	INFO: "INFO",
	WARNING: "WARN",
	ERROR: "ERROR",
	FATAL: "FATAL",
}

var levelColor = map[int]int{
	TRACE         : Cyan,
	DEBUG         : Green,
	INFO          : Blue,
	WARNING       : Purple,
	ERROR         : Red,
	FATAL         : Red,
}

// default
const defaultLevel = TRACE
const defaultColor = false
var defaultStdoutWriter = os.Stdout
var defaultStderrWriter = os.Stderr

func defaultLineBreak() string {
	switch runtime.GOOS {
	case "windows":
		return "\r\n"
	case "linux":
		return "\n"
	default:
		return "\n"
	}
}

func defaultFormatCB(level int, format string, v... interface{}) string {
	return time.Now().Format(time.RFC3339Nano) + " " + fmt.Sprintf(format, v)
}

func New(cb formatCB) *Logger {
	if cb == nil {
		cb = defaultFormatCB
	}

	log := Logger{
		out: nil,
		level: defaultLevel,
		lock: nil,
		format: cb,
		color: defaultColor,
		terminal: true,
	}

	queue := queue{
		enable: false,
		isStop: make(chan bool),
		buf:    make(chan logEntry, 1024),
	}
	log.queue = queue

	return &log
}

func (log *Logger) writeLog(writer io.Writer, color int, format string, v ...interface{}) {
	if log.terminal {
		//ColorWrite(writer, color, format, v...)
	}
	for _, w := range log.out {
		if log.queue.enable {
			log.queue.buf <- logEntry{
				writer: w,
				color: color,
				format: format,
				v: v,
			}
		} else {
			w.Write([]byte(fmt.Sprintf(format, v...)))
		}
	}
}

// mutex sync, output log conf to writer.
func (log *Logger) logOut(level int, format string, v ...interface{}) {
	if level < log.level {
		return
	}

	log.mutex.Lock()
	defer log.mutex.Unlock()

	if log.lock != nil {
		log.lock.Lock()
		defer log.lock.Unlock()
	}

	lName := levelName[level]
	lColor := levelColor[level]

	var fmtStr string
	if log.format != nil {
		fmtStr = log.format(level, format, v...)
	} else {
		fmtStr = fmt.Sprintf("%s", v...)
	}

	fmtStr += defaultLineBreak()

	var writer io.Writer
	switch level {
	case TRACE:
		fallthrough
	case DEBUG:
		fallthrough
	case WARNING:
		fallthrough
	case INFO:
		writer = defaultStdoutWriter
	case ERROR:
		fallthrough
	case FATAL:
		writer = defaultStderrWriter
	default:
		writer = defaultStderrWriter
	}

	log.writeLog(writer, lColor, lName + " ")

	if !log.color {
		lColor = White
	}

	log.writeLog(writer, lColor, fmtStr)
}

/*
user configure interface
 */

// AddWriters add writer.
func (log *Logger) AddWriters(writer ...Writer) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.out = append(log.out, writer...)
}

// RemoveWriters remove writer.
func (log *Logger) RemoveWriters(writers ...Writer) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	out := log.out
	log.out = nil

	for _, w := range out {
		flag := true
		for _, rw := range writers {
			if rw == w {
				flag = false
				break
			}
		}

		if flag {
			log.out = append(log.out, w)
		}
	}
}

// SetTerminal set logger level.
func (log *Logger) SetTerminal(flag bool) {
	log.mutex.Lock()
	defer log.mutex.Unlock()
}

func (log *Logger) SetLevel(level int){
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.level = level
}

// GetLevel set logger level.
func (log *Logger) GetLevel() int {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	return log.level
}

// SetLock set lock.
func (log *Logger) SetLock(lock Lock) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.lock = lock
}

// GetLock get lock.
func (log *Logger) GetLock() Lock {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	return log.lock
}

// SetColor set display color.
func (log *Logger) SetColor(flag bool) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.color = flag
}

// GetColor get display status.
func (log *Logger) GetColor() bool {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	return log.color
}

// SetFormatCB set format callback.
func (log *Logger) SetFormatCB(cb formatCB) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.format = cb
}

// GetFormatCB get format callback.
func (log *Logger) GetFormatCB() formatCB {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	return log.format
}

// SetEnableMQ set enable mq
func (log *Logger) SetEnableMQ(flag bool) {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	if flag {
		end:
			for {
				select {
				case <- log.queue.buf:
				default:
					select {
					case <- log.queue.isStop:
							break end
					}
				}
			}
	} else {
		log.queue.isStop <- true
	}

	log.queue.enable = false
}

// GetEnableMQ get enable mq
func (log *Logger) GetEnableMQ() bool {
	log.mutex.Lock()
	log.mutex.Unlock()

	return log.queue.enable
}

func (log *Logger) BlankLine() {
	log.mutex.Lock()
	defer log.mutex.Unlock()

	log.writeLog(defaultStdoutWriter, White, defaultLineBreak())
}

func (log *Logger) Trace(format string, v... interface{}){
	log.logOut(TRACE, format, v...)
}

func (log *Logger) Debug(format string, v... interface{}){
	log.logOut(DEBUG, format, v...)
}

func (log *Logger) Info(format string, v... interface{}){
	log.logOut(INFO, format, v...)
}

func (log *Logger) Warning(format string, v... interface{}){
	log.logOut(WARNING, format, v...)
}

func (log *Logger) Error(format string, v... interface{}){
	log.logOut(ERROR, format, v...)
}

func (log *Logger) Fatal(format string, v... interface{}){
	log.logOut(FATAL, format, v...)
}

func (log *Logger) B() {
	log.BlankLine()
}

func (log *Logger) T(format string, v... interface{}) {
	log.logOut(TRACE, format, v...)
}

func (log *Logger) D(format string, v... interface{}){
	log.logOut(DEBUG, format, v...)
}

func (log *Logger) I(format string, v... interface{}){
	log.logOut(INFO, format, v...)
}

func (log *Logger) W(format string, v... interface{}){
	log.logOut(WARNING, format, v...)
}

func (log *Logger) E(format string, v... interface{}){
	log.logOut(ERROR, format, v...)
}

func (log *Logger) F(format string, v... interface{}){
	log.logOut(FATAL, format, v...)
}