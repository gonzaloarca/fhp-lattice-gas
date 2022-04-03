
import argparse
import os
import matplotlib.pyplot as plot
import numpy as np

def vary_particles(cut_condition_name, threshold, iterations): 
    number_of_particles = [2000, 3000, 5000, 6500]

    particles_count = []
    steps = [] 
    lower_error = []
    upper_error = []

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
                balance_step = int(lines[-1].split("\t")[0])

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

    print(f"Particles: {particles_count}")
    print(f"Steps: {steps}")
    print(f"Lower error: {lower_error}")
    print(f"Upper error: {upper_error}")

    plot.errorbar(particles_count, steps, ls="none", yerr=[lower_error, upper_error], color='red', marker='o', ecolor="blue", elinewidth=0.5, capsize=5)
    plot.ylabel("Cantidad de iteraciones")
    plot.xlabel("Cantidad de particulas")
    plot.show()
    plot.close()



def vary_slit_width(cut_condition_name, threshold, slit_width_step, iterations):
    N = 3000
    max_slit_width = 200
    slit_widths = [slit_width for slit_width in range(slit_width_step, max_slit_width + slit_width_step, slit_width_step)]

    slit_width_count = []
    steps = [] 
    lower_error = []
    upper_error = []

    for slit_width in slit_widths:

        max_step = 0
        min_step = 0
        step = 0

        for iteration in range(iterations):

            cmd = f"java -DN={int(N)} -DD={slit_width} -DcutCondition={cut_condition_name} -Dthreshold={threshold} -jar ../target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Iteration: {iteration}. Executing: {cmd}")
            os.system(cmd)

            with open("RightParticles.txt") as right_particles_file:
                lines = right_particles_file.readlines()

                # Last line has an enter
                balance_step = int(lines[-1].split("\t")[0])

                if min_step == 0:
                    min_step = balance_step

                if balance_step > max_step:
                    max_step = balance_step
                elif balance_step < min_step:
                    min_step = balance_step

                step += balance_step

        slit_width_count.append(slit_width)
        average_step = step / iterations
        steps.append(average_step)
        lower_error.append(average_step - min_step)
        upper_error.append(max_step - average_step)

    print(f"Slit widths: {slit_width_count}")
    print(f"Steps: {steps}")
    print(f"Lower error: {lower_error}")
    print(f"Upper error: {upper_error}")
    
    plot.errorbar(slit_width_count, steps, ls="none", yerr=[lower_error, upper_error], color='red', marker='o', ecolor="blue", elinewidth=0.5, capsize=5)
    plot.ylabel("Cantidad de iteraciones")
    plot.xlabel("Apertura en el tabique")
    plot.show()
    plot.close()


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser()
    parser.add_argument("--vary", default=None, help="Variable to change", dest="vary", required=True)
    parser.add_argument("--slit-width-step", default=25, help="The slit width step", dest="slit_width_step", required=False)
    parser.add_argument("--iterations", default=5, help="The number of iterations per number of particles", dest="iterations", required=False)

    args = parser.parse_args()

    cut_condition_name = "ParticlesPerSide"
    threshold = 0.1

    if args.vary == "N":
        vary_particles(cut_condition_name, threshold, int(args.iterations))
    elif args.vary == "D":
        vary_slit_width(cut_condition_name, threshold, int(args.slit_width_step), int(args.iterations))
    else:
        print("Invalid variation")