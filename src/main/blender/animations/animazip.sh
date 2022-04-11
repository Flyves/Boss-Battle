#!/bin/sh
cd ".\boss_archv" || exit
rm ./*
"C:\Program Files\7-Zip\7z.exe" a -v8m "boss animations.zip" "..\boss\*.blend"