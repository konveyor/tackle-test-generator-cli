#!/bin/bash

SF110_35=(
    1_tullibee
    2_a4j
    3_gaj
    4_rif
    5_templateit
    6_jnfe
    7_sfmis
    14_omjstate
    16_templatedetails
    19_jmca
    20_nekomud
    21_geo-google
    22_byuic
    23_jwbf
    25_jni-inchi
    28_greencow
    30_bpmail
    32_httpanalyzer
    39_diffi
    40_glengineer
    42_asphodel
    46_nutzenportfolio
    48_resources4j
    49_diebierse
    53_shp2kml
    54_db-everywhere
    55_lavalamp
    58_fps370
    59_mygrid
    64_jtailgui
    67_gae-app-manager
    74_fixsuite
    76_dash-framework
    77_io-project
    90_dcparseargs
)

SF110_59=(
    1_tullibee
    2_a4j
    3_gaj
    4_rif
    5_templateit
    6_jnfe
    7_sfmis
    8_gfarcegestionfa
    9_falselight
    11_imsmart
    14_omjstate
    15_beanbin
    16_templatedetails
    19_jmca
    20_nekomud
    21_geo-google
    22_byuic
    23_jwbf
    25_jni-inchi
    26_jipa
    29_apbsmem
    30_bpmail
    31_xisemele
    32_httpanalyzer
    37_petsoar
    39_diffi
    40_glengineer
    41_follow
    42_asphodel
    45_lotus
    46_nutzenportfolio
    47_dvd-homevideo
    48_resources4j
    49_diebierse
    50_biff
    51_jiprof
    53_shp2kml
    55_lavalamp
    58_fps370
    59_mygrid
    60_sugar
    62_dom4j
    64_jtailgui
    66_openjms
    67_gae-app-manager
    71_ext4j
    74_fixsuite
    76_dash-framework
    77_io-project
    81_javathena
    82_ipcalculator
    85_shop
    86_at-robots2-j
    87_jaw-br
    90_dcparseargs
    95_celwars2009
    98_trans-locator
    100_jgaap
)

apps=( ${SF110_59[@]} )

echo "Running on ${apps[*]}"

mkdir -p tkltest_log

for app in ${apps[@]}; do
	echo "§§§ $app §§§"
	python3 tkltest_entry.py $app $@ 2>&1 | tee -a tkltest_log/log_$app.out &
done

wait
echo "§§§ Cumulating... §§§"
python3 tkltest_entry.py -HC ${apps[@]} $@ 2>&1 | tee -a tkltest_log/log_cumulate.out

