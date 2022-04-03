
import os
import matplotlib.pyplot as plot

from utils import right_particles_fraction_counter

def right_particles_fraction_plot(threshold): 
    number_of_particles = [2000, 3000, 5000]

    for particles in number_of_particles:
        file_name = 'gas_simulation.txt'
        cmd = f"java -DN={int(particles)} -Dthreshold={threshold} -DoutFileName={file_name} -jar ./target/FHPLatticeGas-1.0-SNAPSHOT.jar"
        print(f"Executing: {cmd}")
        os.system('pwd')
        os.system(cmd)

        right_particles_fractions_count = right_particles_fraction_counter(file_name)
        steps = range(0, len(right_particles_fractions_count))
        print(f"Right particles fractions: {right_particles_fractions_count}")
        plot.plot(steps, right_particles_fractions_count)

    plot.xlabel("Número de iteración")
    plot.ylabel("Fracción de particulas en el recinto derecho")

    plot.legend(["2000 particulas", "3000 particulas", "5000 particulas"])
    plot.show()
    plot.close()


if __name__ == "__main__":
    
    threshold = 0.1
    right_particles_fraction_plot(threshold)

  
    