set terminal pdf
set output "figure-06-a.pdf"

set xrange [0:1]
set yrange [0:1]
set grid
plot "figure-06-a.txt" with circles, "figure-06-a-rp.txt" with points