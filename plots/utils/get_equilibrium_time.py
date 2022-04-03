def get_equilibrium_time(summary_file_name):
    with open(summary_file_name, "r") as summary_file:
        lines = summary_file.readlines()

        return int(lines[0].split()[0])
