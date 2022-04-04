
import argparse
import os
import matplotlib.pyplot as plot

from utils import right_particles_fraction_counter


def right_particles_fraction_plot(threshold, slit_width):
    particle_input = [2000, 3000, 5000]
    simulation_file_name = 'gas_simulation.txt'

    for particle_count in particle_input:
        cmd = f"java -D={slit_width} -DN={particle_count} -Dthreshold={threshold} -DsimulationOutFileName={simulation_file_name} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
        print(f"Executing: {cmd}")
        os.system(cmd)

        right_particles_fractions_count = right_particles_fraction_counter(
            simulation_file_name)
        steps = range(0, len(right_particles_fractions_count))
        print(f"Right particles fractions: {right_particles_fractions_count}")
        plot.plot(steps, right_particles_fractions_count)

    plot.xlabel("Número de iteración")
    plot.ylabel("Fracción de particulas en el recinto derecho")

    plot.legend(["2000 particulas", "3000 particulas", "5000 particulas"])
    plot.show()
    plot.close()


if __name__ == "__main__":

    parser = argparse.ArgumentParser()

    parser.add_argument("--threshold", default=0.1,
                        help="The cut condition threshold used in the simulations. Defaults to 0.1.", dest="threshold", required=False)
    parser.add_argument("--slit_width", default=100,
                        help="The fixed slit width used in the simulations. Defaults to 50.", dest="slit_width", required=False)

    args = parser.parse_args()

    right_particles_fraction_plot(float(args.threshold), int(args.slit_width))
