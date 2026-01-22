# Output settings
set terminal pngcairo size 800,600 enhanced font "Arial,10"
set output "scaling.png"

# Title and labels
set title "Strong Scaling: 10k x 10k Matrix"
set xlabel "Number of Workers"
set ylabel "Execution Time (seconds)"

# Axes formatting
set grid
set logscale x 2
set xtics (1, 2, 4, 8, 16)

# Plot style
set style data linespoints
set pointsize 1.2

# Plot command
plot "scaling.dat" using 1:2 title "Measured Runtime" lc rgb "blue"