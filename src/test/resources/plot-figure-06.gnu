set size square

set xrange [0:1]
set yrange [0:1]

set grid

set style line 1 linetype 1 linewidth 2 pointtype 3 linecolor rgb "aquamarine"


plot "s1.txt"  using 1:2:(sprintf("(%.2f, %.2f)", $1, $2)) with labels point pt 7 ps 2 lc rgb "blue" offset char 6,-0.25 title "S1", "s2.txt"  using 1:2:(sprintf("(%.2f, %.2f)", $1, $2)) with labels point  pt 7 ps 1 lc rgb "green" offset char 6,-0.25 title "S2"