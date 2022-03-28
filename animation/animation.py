import argparse


def calculate_cell_direction():
    pass


def write_static_lattice_file(out_filename, lattice_width, lattice_height):
    # hexagonal lattice

    static_lattice_file = open(out_filename, 'w')

    static_lattice_file.write(f'{lattice_width*lattice_height}\n\n')

    for y in range(lattice_height):
        for x in range(lattice_width):

            if y % 2 == 0:
                x_offset = 0.5
            else:
                x_offset = 0

            static_lattice_file.write(f'{x + x_offset}\t{y*0.866}\t0\n')

    static_lattice_file.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=argparse.FileType('r'),
                        default=None, help="File of lattice simulation timesteps", dest="lattice_steps", required=True)

    args = parser.parse_args()

    xyz_file = open("animation.xyz", "w")

    # N
    # comment
    # xi yi 0

    with args.lattice_steps as lattice_steps_file:
        lines = lattice_steps_file.readlines()
        N = int(lines[0].split()[0])
        D = int(lines[1].split()[0])
        lattice_height = int(lines[2].split()[0])
        lattice_width = int(lines[2].split()[1])

        write_static_lattice_file(
            "static_lattice.xyz", lattice_width, lattice_height)

        lines = lines[3:]

        # parsing and reformatting file for punctual particle animation
        for line in lines:
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
