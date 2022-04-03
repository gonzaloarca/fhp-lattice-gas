
import argparse
import os
import matplotlib.pyplot as plot
import numpy as np

from utils import get_equilibrium_time

def vary_particles(threshold, sample_size, min_particles, max_particles, particle_step, slit_width): 
    particle_input = [particles for particles in range(min_particles, max_particles + particle_step, particle_step)]

    eq_steps = [] 
    lower_error = []
    upper_error = []

    simulation_file_name = "gas_simulation.txt"
    summary_file_name = "summary.txt"

    for particle_count in particle_input:

        max_eq_step = 0
        min_eq_step = 0
        eq_step_sum = 0

        for sample_number in range(sample_size):

            cmd = f"java -DN={int(particle_count)} -DD={slit_width}  -DsimulationOutFileName={simulation_file_name} -DsummaryOutFileName={summary_file_name} -Dthreshold={threshold} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Sample number: {sample_number}. Executing: {cmd}")
            os.system(cmd)

            equilibrium_step = get_equilibrium_time(summary_file_name)

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



def vary_slit_width(threshold, sample_size, min_slit_width, max_slit_width, slit_width_step, particle_count):
    slit_width_input = [slit_width for slit_width in range(min_slit_width, max_slit_width + slit_width_step, slit_width_step)]

    eq_steps = [] 
    lower_error = []
    upper_error = []

    simulation_file_name = "gas_simulation.txt"
    summary_file_name = "summary.txt"

    for slit_width in slit_width_input:

        max_eq_step = 0
        min_eq_step = 0
        eq_step_sum = 0

        for sample_number in range(sample_size):

            cmd = f"java -DN={particle_count} -DD={slit_width} -DsimulationOutFileName={simulation_file_name} -DsummaryOutFileName={summary_file_name} -Dthreshold={threshold} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
            print(f"Sample number: {sample_number}. Executing: {cmd}")
            os.system(cmd)

            equilibrium_step = get_equilibrium_time(summary_file_name)

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
    parser.add_argument("--min_particles", default=1000, help="The minimum number of particles in the simulation. Defaults to 10. Only used when N is the independent variable", dest="min_particles", required=False)
    parser.add_argument("--max_particles", default=6000, help="The maximum number of particles in the plot. Defaults to 6000. Only used when N is the independent variable", dest="max_particles", required=False)
    parser.add_argument("--particle_step", default=1000, help="The step size for the particle count in the plot. Defaults to 1000. Only used when N is the independent variable", dest="particle_step", required=False)
    parser.add_argument("--min_slit_width", default=10, help="The minimum slit width in the simulation. Defaults to 10. Only used when D is the independent variable", dest="min_slit_width", required=False)
    parser.add_argument("--max_slit_width", default=100, help="The maximum slit width in the plot. Defaults to 100. Only used when D is the independent variable", dest="max_slit_width", required=False)
    parser.add_argument("--slit_width_step", default=10, help="The step size for the slit width in the plot. Defaults to 10. Only used when D is the independent variable", dest="slit_width_step", required=False)
    parser.add_argument("--particle_count", default=3000, help="The fixed number of particles used in the simulations. Defaults to 3000. Only used when D is the independent variable", dest="particle_count", required=False)
    parser.add_argument("--slit_width", default=100, help="The fixed slit width used in the simulations. Defaults to 100. Only used when N is the independent variable", dest="slit_width", required=False)


    args = parser.parse_args()

    threshold = 0.1

    if args.var == "N":
        vary_particles(threshold, int(args.sample_size), int(args.min_particles), int(args.max_particles), int(args.particle_step), int(args.slit_width))
    elif args.var == "D":
        vary_slit_width(threshold, int(args.sample_size), int(args.min_slit_width), int(args.max_slit_width), int(args.slit_width_step), int(args.particle_count))
    else:
        print("Invalid argument value for independent variable. Use N for particle count or D for slit width")