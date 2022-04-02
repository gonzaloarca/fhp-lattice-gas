
import os
import matplotlib.pyplot as plot


if __name__ == "__main__":
    
    number_of_particles = [2000, 3000, 5000]
    cutConditionName = "ParticlesPerSide"
    threshold = 0.1

    for particles in number_of_particles:

        cmd = f"java -DN={int(particles)} -DcutCondition={cutConditionName} -Dthreshold={threshold} -jar ../target/FHPLatticeGas-1.0-SNAPSHOT.jar"
        print(f"Executing: {cmd}")
        os.system(cmd)

        with open("RightParticles.txt") as right_particles_file:
            right_particles = right_particles_file.readlines()

            steps = []
            right_particles_count = []

            for line in right_particles:
                line_data = line.split("\t")

                steps.append(int(line_data[0]))
                right_particles_count.append(int(line_data[1]))

            plot.plot(steps, right_particles_count)

    plot.xlabel("Iteraciones")
    plot.ylabel("Cantidad de particulas en la derecha")

    plot.legend(["2000 particulas", "3000 particulas", "5000 particulas"])
    # plot.title(f"{cutConditionName} = {threshold}")
    plot.show()
    plot.close()
