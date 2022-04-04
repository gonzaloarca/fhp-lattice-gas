import argparse


def write_animation(out_filename, input_file):
    xyz_file = open(out_filename, "w")
    with input_file as lattice_steps_file:
        for i, line in enumerate(lattice_steps_file):
            if i == 0:
                N = int(line.split()[0])
            elif i > 2:
                line_data = line.split("\t")
                # timestep
                if len(line_data) == 1:
                    xyz_file.write(f'{N}\ncomment\n')
                else:
                    current_directions = [
                        direction for direction in line_data[2:8] if int(direction) == 1]
                    sum_val = len(current_directions)
                    for i in range(sum_val):
                        x = int(line_data[0])
                        y = int(line_data[1])

                        if y % 2 == 0:
                            x_offset = 0.5
                        else:
                            x_offset = 0

                        xyz_file.write(f'{x + x_offset}\t{y*0.866}\t0\n')
    xyz_file.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=argparse.FileType('r'),
                        default=None, help="File of lattice simulation timesteps", dest="lattice_steps", required=True)
    parser.add_argument("--output", type=str,
                        default="particles.xyz", help="Desired file name for XYZ output", dest="out_file_name", required=True)

    args = parser.parse_args()

    write_animation(args.out_file_name, args.lattice_steps)
