set terminal pngcairo size 1100,700 enhanced font "Arial,10"
set output "qthreads_by_threads.png"

set title "Execution Time vs Size (separate line per thread count)"
set xlabel "Size"
set ylabel "Execution Time (seconds)"

set grid
set key outside right
set pointsize 1.0

# Logarithmic X axis for size
set logscale x 10
set logscale y 10

# Data is comma-separated into 3 fields per line
set datafile separator ","

# Parsing:
# field 1: "size: <N>"  -> word(strcol(1),2)
# field 2: "The execution time is <T> sec" -> word(strcol(2),5)
# field 3: "threads: <K>" -> word(strcol(3),2)

plot \
  "qthreads.txt" using \
    ((word(strcol(3),2) eq "1")  ? (word(strcol(1),2)+0) : (1/0)) : \
    ((word(strcol(3),2) eq "1")  ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=1", \
  "" using \
    ((word(strcol(3),2) eq "4")  ? (word(strcol(1),2)+0) : (1/0)) : \
    ((word(strcol(3),2) eq "4")  ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=4", \
  "" using \
    ((word(strcol(3),2) eq "8")  ? (word(strcol(1),2)+0) : (1/0)) : \
    ((word(strcol(3),2) eq "8")  ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=8", \
  "" using \
    ((word(strcol(3),2) eq "16") ? (word(strcol(1),2)+0) : (1/0)) : \
    ((word(strcol(3),2) eq "16") ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=16", \
  "" using \
    ((word(strcol(3),2) eq "32") ? (word(strcol(1),2)+0) : (1/0)) : \
    ((word(strcol(3),2) eq "32") ? (word(strcol(2),5)+0) : (1/0)) \
    with linespoints pt 7 lw 2 title "threads=32"