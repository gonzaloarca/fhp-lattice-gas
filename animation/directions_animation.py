import argparse
import enum
from io import TextIOWrapper
from itertools import islice, starmap

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


# (x,y)
class Direction(enum.Enum):
    A = (1, 0)
    B = (1, 1)
    C = (-1, 1)
    D = (-1, 0)
    E = (-1, -1)
    F = (1, -1)

    def __iter__(self):
        return iter(self.value)


def process_subgrids(lattice_width, lattice_height, subgrids, subgrid_width, subgrid_height, max_particles_per_subgrid, out_file: TextIOWrapper):
    for i in range(lattice_height//subgrid_height):
        for j in range(lattice_width//subgrid_width):
            subgrid_y = (j * subgrid_width) + (subgrid_width / 2)
            subgrid_x = (i * subgrid_height) + (subgrid_height / 2)
            if subgrid_y % 2 == 0:
                x_offset = 0.5
            else:
                x_offset = 0
            dir_particle = [0, 0, 0, 0, 0, 0]
            new_direction = (0, 0)
            particles = subgrids[i][j]
            for particle in particles:
                if particle.A is not None:
                    dir_particle[0] += 1
                elif particle.B is not None:
                    dir_particle[1] += 1
                elif particle.C is not None:
                    dir_particle[2] += 1
                elif particle.D is not None:
                    dir_particle[3] += 1
                elif particle.E is not None:
                    dir_particle[4] += 1
                elif particle.F is not None:
                    dir_particle[5] += 1

            products = [m for m in starmap(
                lambda i, j:i*j, [y for x in zip(Direction.A, Direction.B, Direction.C, Direction.D, Direction.E, Direction.F) for y in zip(x, dir_particle)])]
            new_direction = tuple(sum(x) for x in zip(
                *[islice(products, i, None, 6) for i in range(6)]))
            new_direction_normalized = tuple(
                component/max_particles_per_subgrid for component in new_direction)
            out_file.write(
                f'{subgrid_x + x_offset}\t{subgrid_y * 0.866}\t0\t{new_direction_normalized[0]}\t{new_direction_normalized[1]}\n')


def write_directions(input_file, subgrid_width, subgrid_height, out_filename):

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
                        process_subgrids(lattice_width, lattice_height,
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
                    subgrid.append(Particle(int(line_data[0]), int(line_data[1]), 0, Direction.A if int(line_data[2]) == 1 else None, Direction.B if int(line_data[3]) == 1 else None, Direction.C if int(line_data[4]) == 1 else None,
                                            Direction.D if int(line_data[5]) == 1 else None, Direction.E if int(line_data[6]) == 1 else None, Direction.F if int(line_data[7]) == 1 else None, True if int(line_data[8]) == 1 else False, True if int(line_data[9]) == 1 else False, True if int(line_data[10]) == 1 else False))

    subgrid_file.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=argparse.FileType('r'),
                        default=None, help="File of subgrids simulation timesteps", dest="lattice_steps", required=True)
    parser.add_argument("--output", type=argparse.FileType('r'),
                        default="average_vector_field.xyz", help="Desired file name for XYZ output", dest="out_file_name", required=True)

    args = parser.parse_args()

    write_directions(args.lattice_steps, 10, 10, args.out_file_name)
