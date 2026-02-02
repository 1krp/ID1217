# ==========================================
# Quicksort: time vs size, one averaged line per thread
# Y: time (milliseconds), X: problem size
# Input: size, threads, time_seconds
# ==========================================

rawfile   = "quicksortData.txt"
cleanfile = "qs_clean.dat"
avgfile   = "qs_avg.dat"

# --- Step 1: normalize input -> whitespace columns: size threads time_s
# Handles commas + optional spaces
cmd1 = sprintf("awk -F'[ ,]+' 'NF>=3 {print $1, $2, $3}' %s > %s", rawfile, cleanfile)
system(cmd1)

# --- Step 2: average time per (size,threads) and sort by thread then size
cmd2 = sprintf(\
"awk '{sum[$1,$2]+=$3; cnt[$1,$2]++} \
END{for (k in sum){split(k,a,SUBSEP); print a[1], a[2], sum[k]/cnt[k]}}' %s \
| sort -k2,2n -k1,1n > %s", cleanfile, avgfile)
system(cmd2)

# --- Output (PDF) ---
set terminal pdfcairo enhanced font "Arial,11"
set output "quicksort_time_vs_size_ms.pdf"

# --- Plot cosmetics ---
set title "Quicksort: Average Time vs Problem Size"
set xlabel "Problem size (N)"
set ylabel "Time (ms)"
set grid
set key outside right
set style data lines
set datafile separator whitespace

# Thread set (from your file; edit if needed)
threads = "1 2 4 8 16 32 64 128"

# --- Plot: seconds -> milliseconds (*1000) ---
plot for [t in threads] avgfile using \
    ( $2==int(t) ? $1 : 1/0 ) : \
    ( $2==int(t) ? $3*1000.0 : 1/0 ) \
    with lines lw 2 \
    title sprintf("%s threads", t)

unset output
