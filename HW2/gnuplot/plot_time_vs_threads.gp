# ==========================================
# Time vs Threads (one averaged line per size)
# Y: milliseconds, X: thread count
# ==========================================

rawfile   = "quicksortData1m.txt"
cleanfile = "clean.dat"
avgfile   = "avg.dat"

# -------------------------------------------------
# Step 1 — Extract numeric columns
# -------------------------------------------------
cmd1 = sprintf("awk -F'[ ,]+' 'NF>=3 {print $1, $2, $3}' %s > %s", rawfile, cleanfile)
system(cmd1)

# -------------------------------------------------
# Step 2 — Average time per (size,threads)
# -------------------------------------------------
cmd2 = sprintf(\
"awk '{sum[$1,$2]+=$3; cnt[$1,$2]++} \
END{for (k in sum){split(k,a,SUBSEP); print a[1], a[2], sum[k]/cnt[k]}}' %s \
| sort -k1,1n -k2,2n > %s", cleanfile, avgfile)
system(cmd2)

# -------------------------------------------------
# Plot to PDF
# -------------------------------------------------
set terminal pdfcairo enhanced font "Arial,11"
set output "time_vs_threads_ms.pdf"

set title "Execution Time vs Threads"
set xlabel "Number of Threads"
set ylabel "Time (milliseconds)"
set grid
set key outside right
set style data lines

# Sizes present in file (edit if needed)
size = "1000000"

plot for [s in sizes] avgfile using \
    ( $1==int(s) ? $2 : 1/0 ) : \
    ( $1==int(s) ? $3*1000.0 : 1/0 ) \
    with lines lw 2 \
    title sprintf("N=%s", s)

unset output
