import bpy

# I have no clue where I got the octane model in the blender file.
# You can actually find a lot of these on the internet.
# I removed a lot of triangles on the model I'm using because my pc was having trouble
# to playback in real time animations of 50+ cars (that's blender 2.8 for ya blender devs plz fix this lol).

def main():
    # destination folder
    export_path = "D:\\Program Files\\GitHub\\RocketLeague-PanBot\\src\\main\\resources\\boss rig\\"
    # file extension when exported
    export_extension = ".cop"

    every_scene = bpy.context.window.scene

    # This part is a very bad way of handling strings, but as long as
    # you don't touch the name of the objects in the scene,
    # we should be fine.
    # Feel free to find the file_name in a better way if you want!
    file_name = bpy.path.basename(bpy.context.blend_data.filepath)
    file_name_without_extension = file_name[0:len(file_name)-6]

    file = open(export_path + file_name_without_extension + export_extension, 'w')

    # frames
    for every_frame in range(every_scene.frame_start, every_scene.frame_end+1):
        # frame set
        every_scene.frame_set(every_frame)

        # objects
        for every_object in every_scene.objects:
            if every_object.name[16:19] == "":
                continue
            
            # Get the state matrix of the object.
            # By "state matrix", I mean the matrix that contains the
            # position as well as the orientation matrix of the object.
            state_matrix = every_object.matrix_world

            # Get the scaling vector of the object to scale the orientation matrix back
            # to something normal before writing it to the file.
            local_object_scale = every_object.scale

            # Send the data!
            file.write(
                # car id in blender
                str(every_object.name)[16:19] + ':'
                # frame id
                + str(every_scene.frame_current) + ':'

                # location of object
                + str(state_matrix[0][3] / local_object_scale[0]) + ':'
                + str(state_matrix[1][3] / local_object_scale[1]) + ':'
                + str(state_matrix[2][3] / local_object_scale[2]) + ':'

                # 3x3 rotation matrix
                + str(state_matrix[0][0] / local_object_scale[0]) + ':'
                + str(state_matrix[0][1] / local_object_scale[1]) + ':'
                + str(state_matrix[0][2] / local_object_scale[2]) + ':'
                + str(state_matrix[1][0] / local_object_scale[0]) + ':'
                + str(state_matrix[1][1] / local_object_scale[1]) + ':'
                + str(state_matrix[1][2] / local_object_scale[2]) + ':'
                + str(state_matrix[2][0] / local_object_scale[0]) + ':'
                + str(state_matrix[2][1] / local_object_scale[1]) + ':'
                + str(state_matrix[2][2] / local_object_scale[2]) + '\n'

                # The last 4x4 matrix row (the state_matrix[3] row) is ignored
                # because it seems to always equal (0, 0, 0, 1).
                # It's useless to stream that.
            )
    file.close()

if __name__ == "__main__":
    main()