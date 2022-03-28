import argparse


def calculate_cell_direction():
    pass


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

        lines = lines[2:]
        
        # parsing and reformatting file for punctual particle animation
        for line in lines:
            line_data = line.split("\t")
            
            # timestep
            if len(line_data) == 1:
                xyz_file.write(f'{N}\ncomment\n')
            else:
              current_directions = [direction for direction in line_data[2:8] if int(direction) == 1]
              sum_val = len(current_directions)
              for i in range(sum_val):
                  xyz_file.write(f'{(line_data[0] if int(line_data[0]) % 2 == 0 else int(line_data[0])+0.5)}\t{int(line_data[1])*0.866}\t0\n')

    xyz_file.close()
