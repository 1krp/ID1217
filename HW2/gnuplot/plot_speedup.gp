reset

set terminal pdfcairo enhanced font "Arial,12" size 10in,6in
set output "speedup.pdf"

set title "Quicksort Speedup"
set xlabel "Threads"
set ylabel "Speedup (T1 / Tn)"

set logscale x 2
set xtics (1,2,4,8,16)
set grid
set key left top

plot \
  "speedup.dat" using 1:2 with linespoints lw 2 pt 7 title "Measured speedup", \
  x with lines dt 2 lw 2 title "Ideal (linear)"

unset output
print "Wrote speedup.pdf"
