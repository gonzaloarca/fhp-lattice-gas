
import argparse
import os
import matplotlib.pyplot as plot
import numpy as np

from utils import right_particles_fraction_counter

def vary_particles(threshold, sample_size): 
    particle_input = [2000, 3000, 5000, 6500]

    eq_steps = [] 
    lower_error = []
    upper_error = []

    simulation_file_name = "gas_simulation.txt"

    for particle_count in particle_input:

        max_eq_step = 0
        min_eq_step = 0
        eq_step_sum = 0

        for sample_number in range(sample_size):

            cmd = f"java -DN={int(particle_count)} -Dthreshold={threshold} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Sample number: {sample_number}. Executing: {cmd}")
            os.system(cmd)

            # Last line has an enter
            right_particles_fraction_count = right_particles_fraction_counter(simulation_file_name)
            equilibrium_step = len(right_particles_fraction_count) - 1

            if min_eq_step == 0:
                min_eq_step = equilibrium_step

            if equilibrium_step > max_eq_step:
                max_eq_step = equilibrium_step
            elif equilibrium_step < min_eq_step:
                min_eq_step = equilibrium_step

            eq_step_sum += equilibrium_step
                
        average_eq_step = eq_step_sum / sample_size
        eq_steps.append(average_eq_step)
        lower_error.append(average_eq_step - min_eq_step)
        upper_error.append(max_eq_step - average_eq_step)

    print(f"Particles: {particle_input}")
    print(f"Steps: {eq_steps}")
    print(f"Lower error: {lower_error}")
    print(f"Upper error: {upper_error}")

    plot.errorbar(particle_input, eq_steps, ls="none", yerr=[lower_error, upper_error], color='red', marker='o', ecolor="blue", elinewidth=0.5, capsize=5)
    plot.ylabel("Tiempo de equilibrio")
    plot.xlabel("Cantidad de particulas")
    plot.show()
    plot.close()



def vary_slit_width(threshold, sample_size):
    N = 3000
    max_slit_width = 100
    slit_width_step = 10
    slit_width_input = [slit_width for slit_width in range(slit_width_step, max_slit_width + slit_width_step, slit_width_step)]

    eq_steps = [] 
    lower_error = []
    upper_error = []

    simulation_file_name = "gas_simulation.txt"

    for slit_width in slit_width_input:

        max_eq_step = 0
        min_eq_step = 0
        eq_step_sum = 0

        for sample_number in range(sample_size):

            cmd = f"java -DN={int(N)} -DD={slit_width} -DoutFileName={simulation_file_name} -Dthreshold={threshold} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Sample number: {sample_number}. Executing: {cmd}")
            os.system(cmd)

            right_particles_fraction_count = right_particles_fraction_counter(simulation_file_name)
            equilibrium_step = len(right_particles_fraction_count) - 1

            if min_eq_step == 0:
                min_eq_step = equilibrium_step

            if equilibrium_step > max_eq_step:
                max_eq_step = equilibrium_step
            elif equilibrium_step < min_eq_step:
                min_eq_step = equilibrium_step

            eq_step_sum += equilibrium_step


        average_eq_step = eq_step_sum / sample_size
        eq_steps.append(average_eq_step)
        lower_error.append(average_eq_step - min_eq_step)
        upper_error.append(max_eq_step - average_eq_step)

    print(f"Slit widths: {slit_width_input}")
    print(f"Steps: {eq_steps}")
    print(f"Lower error: {lower_error}")
    print(f"Upper error: {upper_error}")
    
    plot.errorbar(slit_width_input, eq_steps, ls="none", yerr=[lower_error, upper_error], color='red', marker='o', ecolor="blue", elinewidth=0.5, capsize=5)
    plot.ylabel("Tiempo de equilibrio")
    plot.xlabel("Ancho del tabique en número de celdas")
    plot.show()
    plot.close()


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser()
    parser.add_argument("--var", default=None, help="Independent variable in plot. Use N for particle count and D for slit width", dest="var", required=True)
    parser.add_argument("--sample_size", default=5, help="The number of times the simulation is repeated for each point in the plot. Defaults to 5", dest="sample_size", required=False)

    args = parser.parse_args()

    threshold = 0.1

    if args.var == "N":
        vary_particles(threshold, int(args.sample_size))
    elif args.var == "D":
        vary_slit_width(threshold, int(args.sample_size))
    else:
        print("Invalid argument value for independent variable. Use N for particle count or D for slit width")