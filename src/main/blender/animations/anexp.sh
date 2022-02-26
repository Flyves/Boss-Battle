#!/bin/sh
for blendFileNames in */*.blend; do
  "C:\Program Files\Blender Foundation\Blender 3.0\blender" --background "$blendFileNames" --python .\\object_animation_export.py &
done
wait