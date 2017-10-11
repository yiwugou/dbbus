cd `dirname $0`/..

script_dir=./bin

root_dir=${script_dir}/..
lib_dir="${root_dir}/lib"
logs_dir="${root_dir}/logs"
conf_dir="${root_dir}/conf"
var_dir="${root_dir}/var"

pid_file=${logs_dir}/dbbus.pid
out_file=${logs_dir}/dbbus.out

cp=".:${conf_dir}"
for f in ${lib_dir}/*.jar ; do
  cp="${cp}:${f}"
done

if [ ! -e $logs_dir ] ; then
  mkdir ${logs_dir}
fi

# DEFAULT VALUES
jvm_gc_log=${logs_dir}/gc.log

# JVM ARGUMENTS
jvm_direct_memory_size=4g
jvm_direct_memory="-XX:MaxDirectMemorySize=${jvm_direct_memory_size}"
jvm_min_heap_size="1024m"
jvm_min_heap="-Xms${jvm_min_heap_size}"
jvm_max_heap_size="1024m"
jvm_max_heap="-Xmx${jvm_max_heap_size}"

jvm_gc_options="-XX:NewSize=512m -XX:MaxNewSize=512m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:SurvivorRatio=6 -XX:MaxTenuringThreshold=7"
jvm_gc_log_option="-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution "
if [ ! -z "${jvm_gc_log}" ] ; then
  jvm_gc_log_option="${jvm_gc_log_option} -Xloggc:${jvm_gc_log}"
fi

jvm_arg_line="-d64 ${jvm_direct_memory} ${jvm_min_heap} ${jvm_max_heap} ${jvm_gc_options} ${jvm_gc_log_option} -ea"

config_file_option="-config dbbus.properties"


main_class=com.yiwugou.dbbus.example.DbbusMain

cmdline="java -cp ${cp} ${jvm_arg_line} ${main_class} ${config_file_option} $*"
echo $cmdline
$cmdline 2>&1 > ${out_file} &
echo $! > ${pid_file}



