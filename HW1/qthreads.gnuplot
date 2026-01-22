set terminal pngcairo size 1000,650 enhanced font "Arial,10"
set output "qthreads.png"

set title "Execution Time vs Size (by Thread Count)"
set xlabel "Problem Size"
set ylabel "Execution Time (seconds)"

set grid
set key outside right
set pointsize 1.0

# Logarithmic X axis for size
set logscale x 10

# Data records are comma-separated on the measurement lines
set datafile separator ","

# Parse only lines that contain "The execution time"; skip all others via (1/0)
plot \
  "ql1.txt" using \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(1),2)+0) : (1/0)) : \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=1", \
  "ql4.txt" using \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(1),2)+0) : (1/0)) : \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=4", \
  "ql8.txt" using \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(1),2)+0) : (1/0)) : \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=8", \
  "ql16.txt" using \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(1),2)+0) : (1/0)) : \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=16", \
  "ql32.txt" using \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(1),2)+0) : (1/0)) : \
    ((strstrt(strcol(0),"The execution time")>0) ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=32"