
import argparse
import os
import matplotlib.pyplot as plot

def vary_particles(cut_condition_name, threshold): 
    number_of_particles = [2000, 3000, 5000]

    for particles in number_of_particles:

        cmd = f"java -DN={int(particles)} -DcutCondition={cut_condition_name} -Dthreshold={threshold} -jar ../target/FHPLatticeGas-1.0-SNAPSHOT.jar"
        print(f"Executing: {cmd}")
        os.system(cmd)

        with open("RightParticles.txt") as right_particles_file:
            right_particles = right_particles_file.readlines()

            steps = []
            right_particles_count = []

            for line in right_particles:
                line_data = line.split("\t")

                steps.append(int(line_data[0]))
                right_particles_count.append(int(line_data[1]) / particles)

            plot.plot(steps, right_particles_count)

    plot.xlabel("Número de iteración")
    plot.ylabel("Fracción de particulas en el recinto derecho")

    plot.legend(["2000 particulas", "3000 particulas", "5000 particulas"])
    plot.show()
    plot.close()


def vary_slit_width(cut_condition_name, threshold, slit_width_step):
    N = 3000
    max_slit_width = 200
    slit_widths = [slit_width for slit_width in range(slit_width_step, max_slit_width + slit_width_step, slit_width_step)]

    for slit_width in slit_widths:

        cmd = f"java -DN={N} -DD={slit_width} -DcutCondition={cut_condition_name} -Dthreshold={threshold} -jar ../target/FHPLatticeGas-1.0-SNAPSHOT.jar"
        print(f"Executing: {cmd}")
        os.system(cmd)

        with open("RightParticles.txt") as right_particles_file:
            right_particles = right_particles_file.readlines()

            steps = []
            right_particles_count = []

            for line in right_particles:
                line_data = line.split("\t")

                steps.append(int(line_data[0]))
                right_particles_count.append(int(line_data[1]) / N)

            plot.plot(steps, right_particles_count)

    plot.xlabel("Número de iteración")
    plot.ylabel("Fracción de particulas en el recinto derecho")

    plot.legend([f"{slit_width} nodos de ancho" for slit_width in slit_widths])
    plot.show()
    plot.close()


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser()
    parser.add_argument("--vary", default=None, help="Variable to change", dest="vary", required=True)
    parser.add_argument("--slit-width-step", default=25, help="The slit width step", dest="slit_width_step", required=False)

    args = parser.parse_args()

    cut_condition_name = "ParticlesPerSide"
    threshold = 0.1

    if args.vary == "N":
        vary_particles(cut_condition_name, threshold)
    elif args.vary == "D":
        vary_slit_width(cut_condition_name, threshold, int(args.slit_width_step))
    else:
        print("Invalid variation")
    