def count_particles_in_node(node_data):
    node_data = [int(x) for x in node_data[2:8]]
    return sum(node_data)


def is_right_particle(x_coordinate, lattice_width):
    return x_coordinate > lattice_width/2


def right_particles_fraction_counter(simulation_file_name):
    right_particles_count = []
    total_particle_count = 1;

    with open(simulation_file_name) as simulation_file:
        timestep = -1
        for line_number, line in enumerate(simulation_file):
            if line_number == 0:
                # particle count is not needed
                total_particle_count = int(line.split()[0])
                continue
            elif line_number == 1:
                # slit width is not needed
                continue
            elif line_number == 2:
                lattice_width = int(line.split()[1])
                continue

            # timestep data
            if (len(line.split()) == 1):
                # get fraction by dividing by the total_particle_count after adding 
                # all particles in the lattice for the current timestep
                timestep += 1
                right_particles_count.append(0)
                continue

            # node data
            node_data = line.split()
            x_coordinate = int(node_data[0])

            if is_right_particle(x_coordinate, lattice_width):
                right_particles_count[timestep] += (
                    count_particles_in_node(node_data))

    return [c / total_particle_count for c in right_particles_count]
