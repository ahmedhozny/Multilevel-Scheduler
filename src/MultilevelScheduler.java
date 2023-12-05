import javax.management.InstanceAlreadyExistsException;
import java.util.*;

public class MultilevelScheduler {
	final static int quantum_time_rr = 3;
	final static int quantum_time_srt = 2;

	final static boolean GUI_mode = true;
	static Process current_process = null;
	static Integer processing_time = null;

	static PriorityQueue<Process> processesQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.arrival_time));
	static int numberOfProcesses;
	static ArrayList<Process> endedProcesses = new ArrayList<>();
	static Queue<Process> queue0 = new LinkedList<>();
	static PriorityQueue<Process> queue1 = new PriorityQueue<>(Comparator.comparingInt(o -> o.remaining_time));
	static PriorityQueue<Process> queue2 = new PriorityQueue<>(Comparator.comparingInt(o -> o.remaining_time));
	static int clock = 0;

	public static class Process {
		public final int pid, arrival_time, burst_time, priority;
		public int remaining_time, waiting_time;

		public Process(int pid, int arrival_time, int burst_time, int priority) {
			this.pid = pid;
			this.arrival_time = arrival_time;
			this.burst_time = burst_time;
			this.remaining_time = burst_time;
			this.priority = priority;
			this.waiting_time = 0;
		}
	}

	public static Process roundRobin(int startClock, Queue<Process> queue, int quantumTime, ArrayList<Process> endedProcesses) {
		if (current_process != null && current_process.remaining_time == 0) {
			current_process.waiting_time = startClock - current_process.burst_time - current_process.arrival_time;
			endedProcesses.add(current_process);
			describeKernelEvent(1, startClock, current_process);
			current_process = null;
			processing_time = null;
			return roundRobin(clock, queue0, quantum_time_rr, endedProcesses);
		}
		else if (processing_time != null && processing_time == quantumTime) {
			addToQueue(queue, current_process);
			describeKernelEvent(2, startClock, current_process);
			current_process = null;
			processing_time = null;
			return roundRobin(clock, queue0, quantum_time_rr, endedProcesses);
		}

		if (current_process == null) {
			if (queue.size() == 0)
				return srt(clock, queue1, quantum_time_srt, endedProcesses);
			current_process = queue.poll();
			processing_time = 0;
			describeKernelEvent(0, startClock, current_process);
			if (GUI_mode)
				GUI.instance.popFromQueueTable(current_process);
		}

		Process process = current_process;
		processing_time++;
		current_process.remaining_time--;
		return process;
	}

	public static Process srt(int startClock, Queue<Process> queue, int quantumTime, ArrayList<Process> endedProcesses) {
		if (current_process == null && queue.size() == 0)
			return sjf(startClock, queue2, endedProcesses);
		else
			return roundRobin(startClock, queue, quantumTime, endedProcesses);
	}

	public static Process sjf(int startClock, Queue<Process> queue, ArrayList<Process> endedProcesses) {
		if (current_process != null && current_process.remaining_time == 0) {
			current_process.waiting_time = startClock - current_process.burst_time - current_process.arrival_time;
			endedProcesses.add(current_process);
			describeKernelEvent(1, startClock, current_process);
			current_process = null;
			processing_time = null;
			return roundRobin(clock, queue0, quantum_time_rr, endedProcesses);
		}
		if (current_process == null) {
			if (queue.size() == 0)
				return null;
			current_process = queue.poll();
			processing_time = 0;
			describeKernelEvent(0, startClock, current_process);
			if (GUI_mode)
				GUI.instance.popFromQueueTable(current_process);
		}

		Process process = current_process;
		current_process.remaining_time--;

		return process;
	}

	public static void describeEvent(int time, Process process, String event) {
		if (process != null) {
			System.out.println("At t = " + time + " Process " + process.pid + ": " + event);
		} else {
			System.out.println("At t = " + time + ": " + event);
		}
	}
	public static void describeKernelEvent(int event, int time, Process process) {
		switch (event){
			case 0 -> describeEvent(time, process, "entered kernel");
			case 1 -> describeEvent(time, process, "left the kernel");
			case 2 -> describeEvent(time, process, "left the kernel and returned to the queue");
		}
	}

	public static void addToQueue(Queue<Process> queue, Process process) {
		queue.add(process);
		if (GUI_mode)
			GUI.instance.appendToQueueTable(process);
	}
	public static ArrayList<Process> multilevelScheduling() {
		while (GUI_mode || endedProcesses.size() < numberOfProcesses) {
			while (!processesQueue.isEmpty() && processesQueue.peek().arrival_time <= clock) {
				Process selected = processesQueue.poll();
				describeEvent(selected.arrival_time, selected, "arrived the queue");
				switch (selected.priority) {
					case 0 -> addToQueue(queue0, selected);
					case 1 -> addToQueue(queue1, selected);
					case 2 -> addToQueue(queue2, selected);
				}
			}

			if (current_process != null) {
				switch (current_process.priority) {
					case 0 -> roundRobin(clock, queue0, quantum_time_rr, endedProcesses);
					case 1 -> srt(clock, queue1, quantum_time_srt, endedProcesses);
					case 2 -> sjf(clock, queue2, endedProcesses);
				}
			} else {
				Process process = roundRobin(clock, queue0, quantum_time_rr, endedProcesses);
			}
			if (GUI_mode) {
				GUI.instance.updateCurrentProcess(clock, current_process, processing_time);
				GUI.instance.addToKernelTable(current_process, clock);
			}

			clock += 1;

			if (GUI_mode)
				break;
		}

		return endedProcesses;
	}

	public static void printStatistics() {
		double avgWaitingTime = 0;
		System.out.println("---------------------------------");
		endedProcesses.sort(Comparator.comparingInt(p -> p.pid));
		for (Process process : endedProcesses) {
			avgWaitingTime += process.waiting_time;
			System.out.println("Process " + process.pid + " - Waiting Time: " + process.waiting_time + " units");
		}
		avgWaitingTime /= endedProcesses.size();
		System.out.printf("Average waiting time: %.2f%n", avgWaitingTime);
	}

	public static void main(String[] args) {
		Process[] processes = new Process[6];
		processes[0] = new Process(0, 0, 5, 1);
		processes[1] = new Process(1, 1, 8, 0);
		processes[2] = new Process(2, 3, 6, 2);
		processes[3] = new Process(3, 5, 4, 2);
		processes[4] = new Process(4, 8, 2, 1);
		processes[5] = new Process(5, 16, 10, 0);

		processesQueue.addAll(List.of(processes));
		numberOfProcesses = processes.length;

		if (GUI_mode) {
			try {
				new GUI(new LinkedList<>(processesQueue)).setVisible(true);
			} catch (InstanceAlreadyExistsException e) {
				System.out.println(e.getMessage());
			}
		} else {
			multilevelScheduling();
			printStatistics();
		}
	}
}
