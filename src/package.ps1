#package.ps1 - beginning of script

# TODO validate script

# Declare named parameters here...
param(
    [parameter(position=0,mandatory=$true)][string] $class_directory,
    [parameter(position=1,mandatory=$true)][string] $app_name,
    [parameter(position=2,mandatory=$true)][string] $executing_class
)


function main([string] $class_directory, [string] $app_name, [string] $manifest_file) {
    Invoke-Expression "echo 'Hello World'"
    # compile the java code
    Invoke-Expression "javac -classpath $($class_directory) $($executing_class)"
    # create the jar
    Invoke-Expression "jar cvfe $($app_name) $($executing_class) $($class_directory)/*.class"
}

main $class_directory $app_name $executing_class

#package.ps1 - end of script
