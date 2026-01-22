set offsets graph 0.03, graph 0.20, graph 0.05, graph 0.05
set terminal pngcairo size 900,600 enhanced font "Arial,10"
set output "qscale.png"

set title "Execution Time vs Problem Size, With unlimited threads"
set xlabel "Problem Size"
set ylabel "Execution Time (seconds)"

set grid
set key off
set pointsize 1.5
set logscale x 10

# Add padding so right-edge labels are not clipped
set offsets graph 0.03, graph 0.20, graph 0.05, graph 0.05

set datafile separator ","

plot "qscale.txt" \
  using (word(strcol(1),2)+0):(word(strcol(3),5)+0) \
  with linespoints pt 7 lw 2 lc rgb "blue" notitle, \
  "" using (word(strcol(1),2)+0):(word(strcol(3),5)+0):(sprintf("T=%s", word(strcol(2),2))) \
  with labels offset char 1,1 notitle