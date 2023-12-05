import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import javax.management.InstanceAlreadyExistsException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
    static GUI instance;

    public void updateCurrentProcess(int clock, MultilevelScheduler.Process process, Integer time_in_cpu){
        label2.setText(String.valueOf(clock));
        if (process == null)
        {
            label4.setText("Null");
            label6.setText("Null");
            label8.setText("Null");
            progressBar1.setValue(0);
            progressBar1.setMaximum(1);
        } else {
            label4.setText(String.valueOf(process.pid));
            label6.setText(String.valueOf(process.remaining_time));
            label8.setText(String.valueOf(time_in_cpu));
            progressBar1.setMaximum(process.burst_time);
            progressBar1.setValue(process.burst_time - process.remaining_time);
        }
    }

    public void addToKernelTable(MultilevelScheduler.Process process, int start_time) {
        if (process == null) {
                KernelTableModel.addColumn("Idle");
        } else {
                KernelTableModel.addColumn("(" + start_time + ") Process " + process.pid + "(" + (start_time + 1) + ")");
        }

        JScrollBar horizontal = KernelScroll.getHorizontalScrollBar();
        horizontal.setValue(horizontal.getMaximum());

    }

    public void appendToQueueTable(MultilevelScheduler.Process process) {
        switch (process.priority) {
            case 0 -> RoundRobinTableModel.addColumn("Process " + process.pid);
            case 1 -> SRTTableModel.addColumn("Process " + process.pid);
            case 2 -> SJFTableModel.addColumn("Process " + process.pid);

        }
    }

    public void popFromQueueTable(MultilevelScheduler.Process process) {
        switch (process.priority) {
            case 0 -> RoundRobinTableModel.setColumnCount(RoundRobinTable.getColumnCount() - 1);
            case 1 -> SRTTableModel.setColumnCount(SRTTableModel.getColumnCount() - 1);
            case 2 -> SJFTableModel.setColumnCount(SJFTableModel.getColumnCount() - 1);

        }
    }

    public GUI(Queue<MultilevelScheduler.Process> processes) throws InstanceAlreadyExistsException {
        if (instance != null)
            throw new InstanceAlreadyExistsException("Only one main display instance is allowed");

        instance = this;
        this.setTitle("Multi-level Scheduler");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Integer[][] array = new Integer[processes.size()][];
        int n = processes.size();
        for (int i = 0; i < n; i++) {
            MultilevelScheduler.Process process = processes.poll();
            array[i] = new Integer[]{process.pid, process.arrival_time, process.burst_time, process.priority};
        }

        initComponents(array);
    }

    private void initComponents(Integer[][] processes) {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Ahmed Hosny
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        Panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        ProcessesTable = new JTable();
        Panel2 = new JPanel();
        CurrentProcessPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        progressBar1 = new JProgressBar();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        RoundRobinTableScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        RoundRobinTable = new JTable();
        SRTTableScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        SRTTable = new JTable();
        SJFTableScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        SJFTable = new JTable();

        KernelScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        KernelTable = new JTable();
        buttonBar = new JPanel();
        NextButton = new JButton();
        ExitButton = new JButton();
        RoundRobinTableModel = new DefaultTableModel();
        KernelTableModel = new DefaultTableModel();
        SRTTableModel = new DefaultTableModel();
        SJFTableModel = new DefaultTableModel();


        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridLayout(0, 2));

                //======== Panel1 ========
                {

                    //======== scrollPane1 ========
                    {

                        //---- ProcessesTable ----
                        ProcessesTable.setModel(new DefaultTableModel(
                                processes,
                                new String [] {
                                        "Process ID", "Arrival Time", "Burst TIme", "Priority"
                                }
                        ));


                        ProcessesTable.setName("All Processes");
                        ProcessesTable.setShowHorizontalLines(true);
                        ProcessesTable.setShowVerticalLines(true);
                        scrollPane1.setViewportView(ProcessesTable);
                    }

                    GroupLayout Panel1Layout = new GroupLayout(Panel1);
                    Panel1.setLayout(Panel1Layout);
                    Panel1Layout.setHorizontalGroup(
                            Panel1Layout.createParallelGroup()
                                    .addGroup(Panel1Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                    Panel1Layout.setVerticalGroup(
                            Panel1Layout.createParallelGroup()
                                    .addGroup(Panel1Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                }
                contentPanel.add(Panel1);

                //======== Panel2 ========
                {
                    Panel2.setLayout(new GridLayout(5, 1));

                    //======== CurrentProcessPanel ========
                    {

                        //---- label1 ----
                        label1.setText("Current Tick");

                        //---- label2 ----
                        label2.setText("0");

                        //---- label3 ----
                        label3.setText("Current Process");

                        //---- label4 ----
                        label4.setText("NULL");

                        //---- label5 ----
                        label5.setText("Remaining Time");

                        //---- label6 ----
                        label6.setText("NULL");

                        //---- label7 ----
                        label7.setText("Current Processing TIme");

                        //---- label8 ----
                        label8.setText("NULL");

                        //---- label9 ----
                        label9.setText("Overall Progress");

                        GroupLayout CurrentProcessPanelLayout = new GroupLayout(CurrentProcessPanel);
                        CurrentProcessPanel.setLayout(CurrentProcessPanelLayout);
                        CurrentProcessPanelLayout.setHorizontalGroup(
                                CurrentProcessPanelLayout.createParallelGroup()
                                        .addGroup(CurrentProcessPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(label1, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                                        .addComponent(label3, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                                        .addComponent(label5, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                                        .addComponent(label7, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                                        .addComponent(label9, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                                                .addGap(91, 91, 91)
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addComponent(progressBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(CurrentProcessPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(label4, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(label2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(CurrentProcessPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(label8, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(label6, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addContainerGap(146, Short.MAX_VALUE))
                        );
                        CurrentProcessPanelLayout.setVerticalGroup(
                                CurrentProcessPanelLayout.createParallelGroup()
                                        .addGroup(CurrentProcessPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addComponent(label1)
                                                        .addComponent(label2))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addComponent(label3)
                                                        .addComponent(label4))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addGroup(CurrentProcessPanelLayout.createSequentialGroup()
                                                                .addComponent(label5)
                                                                .addGap(20, 20, 20))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, CurrentProcessPanelLayout.createSequentialGroup()
                                                                .addComponent(label6)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addComponent(label7)
                                                        .addComponent(label8))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(CurrentProcessPanelLayout.createParallelGroup()
                                                        .addComponent(progressBar1, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addContainerGap(25, Short.MAX_VALUE))
                        );
                    }
                    Panel2.add(CurrentProcessPanel);

                    //======== RoundRobinTableScroll ========
                    {
                        //---- RoundRobinTable ----
                        RoundRobinTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        RoundRobinTable.setModel(RoundRobinTableModel);
                        RoundRobinTable.setShowVerticalLines(true);
                        RoundRobinTable.setShowHorizontalLines(true);
                        RoundRobinTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        RoundRobinTableScroll.setViewportView(RoundRobinTable);
                    }

                    Panel2.add(RoundRobinTableScroll);

                    //======== RoundRobinTableScroll ========
                    {
                        //---- RoundRobinTable ----
                        SRTTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        SRTTable.setModel(SRTTableModel);
                        SRTTable.setShowVerticalLines(true);
                        SRTTable.setShowHorizontalLines(true);
                        SRTTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        SRTTableScroll.setViewportView(SRTTable);
                    }

                    Panel2.add(SRTTableScroll);

                    //======== RoundRobinTableScroll ========
                    {
                        //---- RoundRobinTable ----
                        SJFTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        SJFTable.setModel(SJFTableModel);
                        SJFTable.setShowVerticalLines(true);
                        SJFTable.setShowHorizontalLines(true);
                        SJFTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        SJFTableScroll.setViewportView(SJFTable);
                    }

                    Panel2.add(SJFTableScroll);

                    //======== KernelScroll ========
                    {
                        //---- KernelTable ----
                        KernelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        KernelTable.setModel(KernelTableModel);
                        KernelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        KernelTable.setShowHorizontalLines(true);
                        KernelTable.setShowVerticalLines(true);
                        KernelScroll.setViewportView(KernelTable);
                    }
                    Panel2.add(KernelScroll);
                }

                contentPanel.add(Panel2);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0};

                //---- NextButton ----
                NextButton.setText("Next Tick");
                buttonBar.add(NextButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));
                NextButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MultilevelScheduler.multilevelScheduling();
                    }}
                );

                //---- ExitButton ----
                ExitButton.setText("Exit");
                buttonBar.add(ExitButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                ExitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MultilevelScheduler.printStatistics();
                        GUI.instance.setVisible(false);
                        System.exit(0);
                    }});
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Ahmed Hosny
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel Panel1;
    private JScrollPane scrollPane1;
    private JTable ProcessesTable;
    private JPanel Panel2;
    private JPanel CurrentProcessPanel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JProgressBar progressBar1;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JScrollPane RoundRobinTableScroll;
    private JTable RoundRobinTable;
    private JScrollPane SRTTableScroll;
    private JTable SRTTable;
    private JScrollPane SJFTableScroll;
    private JTable SJFTable;
    private JScrollPane KernelScroll;
    private JTable KernelTable;
    private JPanel buttonBar;
    private JButton NextButton;
    private JButton ExitButton;
    private DefaultTableModel RoundRobinTableModel;
    private DefaultTableModel SRTTableModel;
    private DefaultTableModel SJFTableModel;

    private DefaultTableModel KernelTableModel;

}
