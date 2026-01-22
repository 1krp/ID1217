set terminal pngcairo size 900,600 enhanced font "Arial,10"
set output "qthreads_vs_time.png"

set title "Execution Time vs Number of Threads, Problemsize = 10,000,000"
set xlabel "Number of Threads"
set ylabel "Execution Time (seconds)"

set grid
set key off
set pointsize 1.5

# Data is comma-separated:
# 1: "size: 100000000"
# 2: "The execution time is <T> sec"
# 3: "threads: <N>"
set datafile separator ","

# X = threads (field 3, word 2)
# Y = execution time (field 2, word 5)
plot "qtheads2.txt" \
  using (word(strcol(3),2)+0):(word(strcol(2),5)+0) \
  with linespoints pt 7 lw 2 lc rgb "blue"