# Frisch – Hasslacher – Pomeau Lattice Gas Model

## Simulación de Sistemas

ITBA 2022 - 1Q

## Authors

- [Serpe, Octavio](github.com/serpe)
- [Quesada, Francisco](github.com/fquesada00)
- [Arca, Gonzalo](github.com/gonzaloarca)

## Dependencies

- [Java 17](https://www.java.com/)
- [Maven](https://maven.apache.org/)
- [Python 3](https://www.python.org/)
- [Pipenv](https://pipenv.readthedocs.io/)

## Compilation

First, the simulation module first needs to be compiled. From the root directory, run the following command:

```bash
## Compile the simulation module and generate executable jar file
mvn clean package
```

This will create a folder called `target` in the root directory, containing the compiled simulation module in a jar file called `FHPLatticeGas-1.0-SNAPSHOT.jar`.

## Usage

After generating the executable jar file from compilation, the project can be run from the root directory.

To install all dependencies used in the animation and plotting modules, run the following command:

```bash
## Install all dependencies
pipenv install
```

### Running only the simulation

To only execute the simulation without generating any plots or animation files, run the following command:

```bash
java -DsimulationOutFileName=${SIMULATION_OUTPUT_FILE_NAME} \
-DsummaryOutFileName=${SUMMARY_OUTPUT_FILE_NAME} \
-DN=${PARTICLE_COUNT} -DD=${SLIT_WIDTH} -Dthreshold${THRESHOLD} \
-jar target/FHPLatticeGas-1.0-SNAPSHOT.jar
```

where:

- `${SIMULATION_OUTPUT_FILE_NAME}` is the name of the file where the simulation output will be written.
- `${SUMMARY_OUTPUT_FILE_NAME}` is the name of the file where the simulation summary will be written.
- `${PARTICLE_COUNT}` is the number of particles to be included during the simulation
- `${SLIT_WIDTH}` is the width of the slit in cells
- `${THRESHOLD}` is the threshold for the cut condition of the simulation. The cut condition is given by `(left_particles - right_particles) / total_particles < ${THRESHOLD}`

### Generating animation files

After running the standalone simulation, the animation modules may be used for generating XYZ files for animations.

**For the particle animation files, run the following command from the root directory**:

```bash
## Enter the Pipenv environment shell
pipenv shell

## Inside the shell, run the following command
python animation/animation.py --input ${SIMULATION_INPUT_FILE_NAME} \
--output ${XYZ_OUTPUT_FILE_NAME}
```

where:

- `${SIMULATION_INPUT_FILE_NAME}`: Name of the input file generated by the simulation module
- `${XYZ_OUTPUT_FILE_NAME}` (Optional): Name of the output file to be generated by the animation module. By default it's `particles.xyz`

**For the average vector field animation files, run the following command from the root directory**:

```bash
## Enter the Pipenv environment shell
pipenv shell

## Inside the shell, run the following command
python animation/directions_animation.py \
--input ${SIMULATION_INPUT_FILE_NAME} --output ${XYZ_OUTPUT_FILE_NAME}
```

where:

- `${SIMULATION_INPUT_FILE_NAME}`: Name of the input file generated by the simulation module
- `${XYZ_OUTPUT_FILE_NAME}` (Optional): Name of the output file to be generated by the animation module. By default it's `average_vector_field.xyz`

**For the density animation files, run the following command from the root directory**:

```bash
## Enter the Pipenv environment shell
pipenv shell

## Inside the shell, run the following command
python animation/density_animation.py \
--input ${SIMULATION_INPUT_FILE_NAME} --output ${XYZ_OUTPUT_FILE_NAME}
```

where:

- `${SIMULATION_INPUT_FILE_NAME}`: Name of the input file generated by the simulation module
- `${XYZ_OUTPUT_FILE_NAME}` (Optional): Name of the output file to be generated by the animation module. By default it's `average_density.xyz`

**Static walls and lattice structure for animation**

When running the standalone simulation, a file called `static_lattice.xyz` will be generated in the root directory. This file contains the lattice structure of the simulation with white-colored walls and black-colored lattice nodes.

> All resulting XYZ files will be saved in the root folder.

### Generate plots for analysis

The following scripts will automatically run the simulation from the jar file generated in compilation and generate plots for analysis using Python and Matplotlib.

**Plot 1: Fraction of particles in right compound throughout time for different particle counts**:

From the root directory, run:

```bash
## Enter the Pipenv environment shell
pipenv shell

## Run the simulations and generate plot
python plots/plot_fraction_right_compound.py --slit_width ${SLIT_WIDTH} \
--threshold ${THRESHOLD}
```

where:

- `${SLIT_WIDTH}` (Optional): Slit width used in the simulations. By default it's 50
- `${THRESHOLD}` (Optional): Cut condition threshold used in the simulations. By default it's 0.1

**Plot 2: Equilibrium time as a function of particles in the system or slit width**:

From the root directory, run:

```bash
## Enter the Pipenv environment shell
pipenv shell

## Run the simulations and generate plot
python plots/equilibrium_time_plot.py --var ${INDEPENDENT_VARIABLE} \
--sample_size ${SAMPLE_SIZE} --particle_step ${PARTICLE_STEP} \
--min_particles ${MIN_PARTICLES} --max_particles ${MAX_PARTICLES} \
--slit_width_step ${SLIT_WIDTH_STEP} --min_slit_width ${MIN_SLIT_WIDTH} \
--max_slit_width ${MAX_SLIT_WIDTH} --particle_count ${PARTICLE_COUNT} \
--slit_width ${SLIT_WIDTH}
```

where:

- `${INDEPENDENT_VARIABLE}`: Variable to be used as the independent variable in the plot. It can be either `N` for particle count or `D` for slit width.
- `${SAMPLE_SIZE}` (Optional): Number of samples to be used in the plot. It can be any integer greater than 1. Defaults to 5.
- `${PARTICLE_STEP}` (Optional): Step size for the particle count to be used in the variable particle count plot. It can be any integer greater than 1. Defaults to 1000.
- `${MIN_PARTICLES}` (Optional): Minimum particle count to be used in the variable particle count plot. It can be any integer greater than 1. Defaults to 1000.
- `${MAX_PARTICLES}` (Optional): Maximum particle count to be used in the variable particle count plot. It can be any integer greater than 1. Defaults to 6000.
- `${SLIT_WIDTH}` (Optional): Slit width to be used in the variable particle count plot. It can be any integer greater than 1 and less than the default lattice height (200). Defaults to 100.
- `${SLIT_WIDTH_STEP}` (Optional): Step size for the slit width to be used in the variable slit width plot. It can be any integer greater than 1 and less than `${MAX_SLIT_WIDTH}`. Defaults to 10.
- `${MIN_SLIT_WIDTH}` (Optional): Minimum slit width to be used in the variable slit width plot. It can be any integer greater than 1 and less than `${MAX_SLIT_WIDTH}`. Defaults to 10.
- `${MAX_SLIT_WIDTH}` (Optional): Maximum slit width to be used in the variable slit width plot. It can be any integer greater than 1 and less than the default lattice height (200). Defaults to 100.
- `${PARTICLE_COUNT}` (Optional): Particle count to be used in the variable slit width plot. It can be any integer greater than 1. Defaults to 3000.

## Simulation output

Every time the simulation is executed, two output files will be generated: the simulation file and the summary file.

The simulation file contains each step of the simulation and all of the particle coordinates and data for each step. Its name is given by the `-DsimulationOutFileName` flag in the command line when running the simulation.

The summary file contains a summary of the parameters and results of the simulation. Its name is given by the `-DsummaryOutFileName` flag in the command line when running the simulation. The output file contains the following format:

### Simulation output file format

```
${PARTICLE_COUNT}
${SLIT_WIDTH}
${LATTICE_HEIGHT}   ${LATTICE_WIDTH}
${TIMESTEP_0}
${CELL_0_X_COORDINATE} ${CELL_0_Y_COORDINATE} ${RIGHT_DIRECTION_BIT} ${UPPER_RIGHT_DIRECTION_BIT} ${UPPER_LEFT_DIRECTION_BIT} ${LEFT_DIRECTION_BIT} ${LOWER_LEFT_DIRECTION_BIT} ${LOWER_RIGHT_DIRECTION_BIT} ${HORIZONTAL_SOLID_BIT} ${VERTICAL_SOLID_BIT} ${RANDOM_BIT}
${CELL_1_X_COORDINATE} ${CELL_1_Y_COORDINATE} ${RIGHT_DIRECTION_BIT} ${UPPER_RIGHT_DIRECTION_BIT} ${UPPER_LEFT_DIRECTION_BIT} ${LEFT_DIRECTION_BIT} ${LOWER_LEFT_DIRECTION_BIT} ${LOWER_RIGHT_DIRECTION_BIT} ${HORIZONTAL_SOLID_BIT} ${VERTICAL_SOLID_BIT} ${RANDOM_BIT}
...
${CELL_N_X_COORDINATE} ${CELL_N_Y_COORDINATE} ${RIGHT_DIRECTION_BIT} ${UPPER_RIGHT_DIRECTION_BIT} ${UPPER_LEFT_DIRECTION_BIT} ${LEFT_DIRECTION_BIT} ${LOWER_LEFT_DIRECTION_BIT} ${LOWER_RIGHT_DIRECTION_BIT} ${HORIZONTAL_SOLID_BIT} ${VERTICAL_SOLID_BIT} ${RANDOM_BIT}
${TIMESTEP_1}
...
```

### Summary output file format

```
${EQUILIBRIUM TIME}
${PARTICLE_COUNT}
${SLIT_WIDTH}
${LATTICE_HEIGHT}   ${LATTICE_WIDTH}
${CUT_CONDITION_THRESHOLD}
```
