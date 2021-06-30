#!/bin/sh

for blendFileNames in */*.blend; do
  "C:\Program Files\Blender Foundation\Blender 2.83\blender" --background "$blendFileNames" --python .\\object_animation_export.py #&
done

#sleep 5