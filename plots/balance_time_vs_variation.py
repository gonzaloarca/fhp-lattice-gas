
import argparse
import os
import matplotlib.pyplot as plot
import numpy as np

def variate_particles(cut_condition_name, threshold, iterations): 
    number_of_particles = [5000]

    particles_count = []
    steps = [] 
    lower_error = []
    upper_error = []
    legends = []

    for particles in number_of_particles:

        max_step = 0
        min_step = 0
        step = 0

        for iteration in range(iterations):

            cmd = f"java -DN={int(particles)} -DcutCondition={cut_condition_name} -Dthreshold={threshold} -jar ../target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Iteration: {iteration}. Executing: {cmd}")
            os.system(cmd)

            with open("RightParticles.txt") as right_particles_file:
                lines = right_particles_file.readlines()

                # Last line has an enter
                balance_step = int(lines[-2].split("\t")[0])

                if min_step == 0:
                    min_step = balance_step

                if balance_step > max_step:
                    max_step = balance_step
                elif balance_step < min_step:
                    min_step = balance_step

                step += balance_step
                

        particles_count.append(particles)
        average_step = step / iterations
        steps.append(average_step)
        lower_error.append(average_step - min_step)
        upper_error.append(max_step - average_step)
        legends.append(f"{particles} particulas")

    print(f"Steps: {steps}")
    print(f"Lower error: {lower_error}")
    print(f"Upper error: {upper_error}")
    plot.errorbar(particles_count, steps, fmt=None, yerr=[lower_error, upper_error], color='red', marker='o', ecolor="blue", elinewidth=0.5, capsize=5)
    plot.ylabel("Número de iteración")
    plot.xlabel("Cantidad de particulas")
    plot.legend(legends)
    plot.show()
    plot.close()



def variate_slit_width(cut_condition_name, threshold, slit_width_step):
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
    parser.add_argument("--iterations", default=3, help="The number of iterations per number of particles", dest="iterations", required=False)

    args = parser.parse_args()

    cut_condition_name = "ParticlesPerSide"
    threshold = 0.1

    if args.vary == "N":
        variate_particles(cut_condition_name, threshold, int(args.iterations))
    elif args.vary == "D":
        variate_slit_width(cut_condition_name, threshold, int(args.slit_width_step))
    else:
        print("Invalid variation")