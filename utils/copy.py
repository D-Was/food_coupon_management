import os
import sys

def copy_java_files(directory):
    if not os.path.isdir(directory):
        print(f"The directory {directory} does not exist.")
        return

    with open("output.txt", "w") as output_file:
        for root, _, files in os.walk(directory):
            for file in files:
                if file.endswith('.java'):
                    file_path = os.path.join(root, file)
                    with open(file_path, "r") as java_file:
                        content = java_file.read()
                        output_file.write(f"{file_path}:\n")
                        output_file.write(f"{content}\n\n")

    print("All .java files have been copied to output.txt.")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py <directory_path>")
    else:
        copy_java_files(sys.argv[1])
