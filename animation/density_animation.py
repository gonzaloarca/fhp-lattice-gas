import argparse
import enum
from io import TextIOWrapper

MAX_PARTICLES_PER_CELL = 6


class Particle():
    def __init__(self, x, y, z, A, B, C, D, E, F, XS, YX, R):
        self.x = x
        self.y = y
        self.z = z
        self.A = A
        self.B = B
        self.C = C
        self.D = D
        self.E = E
        self.F = F
        self.XS = XS
        self.YX = YX
        self.R = R

    def __repr__(self):
        return f'Particle({self.x}, {self.y}, {self.z}, {self.A}, {self.B}, {self.C}, {self.D}, {self.E}, {self.F}, {self.XS}, {self.YX}, {self.R})'


def process_subgrids(N, lattice_width, lattice_height, subgrids, subgrid_width, subgrid_height, max_particles_per_subgrid, out_file: TextIOWrapper):
    for i in range(lattice_height//subgrid_height):
        for j in range(lattice_width//subgrid_width):
            subgrid_y = (j * subgrid_width) + (subgrid_width / 2)
            subgrid_x = (i * subgrid_height) + (subgrid_height / 2)
            num_of_particles = len(subgrids[i][j])
            avg_max_density = N / \
                (lattice_height * lattice_width) * \
                subgrid_height * subgrid_width * 2
            subgrid_particle_density = num_of_particles / avg_max_density
            out_file.write(
                f'{subgrid_x}\t{subgrid_y}\t0\t0\t0\t1\t{1-subgrid_particle_density if subgrid_particle_density != 0 else 1}\t{num_of_particles}\n')


def write_density(input_file, subgrid_width, subgrid_height, out_filename):

    subgrid_file = open(out_filename, "w")
    N = 0
    D = 0
    lattice_height = 0
    lattice_width = 0
    total_subgrids = 0
    subgrids = []
    max_particles_per_subgrid = 0
    with input_file as lattice_steps_file:
        for i, line in enumerate(lattice_steps_file):
            if i == 0:
                N = int(line.split()[0])
            elif i == 1:
                D = int(line.split()[0])
            elif i == 2:
                lattice_height = int(line.split()[0])
                lattice_width = int(line.split()[1])
                total_subgrids = (lattice_height/subgrid_height) * \
                    (lattice_width/subgrid_width)
                subgrids = [[[] for __ in range(
                    lattice_width//subgrid_width)] for _ in range(lattice_height//subgrid_height)]
                max_particles_per_subgrid = subgrid_height * \
                    subgrid_width * MAX_PARTICLES_PER_CELL
            else:
                line_data = line.split()
                # timestep
                if len(line_data) == 1:
                    if(i != 3):
                        subgrid_file.write(f'{int(total_subgrids)}\ncomment\n')
                        process_subgrids(N, lattice_width, lattice_height,
                                         subgrids, subgrid_width, subgrid_height, max_particles_per_subgrid, subgrid_file)
                        subgrids = [[[] for __ in range(
                            lattice_width//subgrid_width)] for _ in range(lattice_height//subgrid_height)]
                else:
                    # add particle to subgrid
                    x = int(line_data[0])
                    y = int(line_data[1])
                    subgrid_x = x//subgrid_width
                    subgrid_y = y//subgrid_height
                    subgrid = subgrids[subgrid_x][subgrid_y]
                    subgrid.append(1)

    subgrid_file.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=argparse.FileType('r'),
                        default=None, help="File of subgrids simulation timesteps", dest="lattice_steps", required=True)

    args = parser.parse_args()

    write_density(args.lattice_steps, 20, 20, "average_density.xyz")
