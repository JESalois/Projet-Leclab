package view;
import contoller.ParameterViewController;
import experiment.Experimentation;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import parameters.EParameterPosition;
import parameters.NoiseParameter;
import parameters.TactileDistractorParameter;
import parameters.VisualDistractorParameter;
import parameters.geometry.AbstractShape;
import parameters.geometry.Circle;
import parameters.geometry.Diamond;
import parameters.geometry.Hexagon;
import parameters.geometry.Octogon;
import parameters.geometry.Pentagon;
import parameters.geometry.Pentagram;
import parameters.geometry.Rectangle;
import parameters.geometry.Trapeze;
import parameters.geometry.Triangle;
import utility.FolderReader;
import utility.Global;

/**
 *
 * @author Jean-Etienne Salois
 */
public class ParameterView extends javax.swing.JFrame {
    /**
     * This is the reference to the controller of this view in charge of
     * updating the model.
     * @see ParameterViewController
     */
    private ParameterViewController m_controller;
    
    /**
     * Creates new form ParameterView
     */
    public ParameterView() {
        initComponents();
        m_controller = new ParameterViewController();
        
        //Place jFrame at the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width - this.getSize().width)/2, (dim.height - this.getSize().height)/2);
        
        this.loadNoiseComboPanel();
        
        // Assign existing value in the view
        VisualDistractorParameter vdp = (VisualDistractorParameter)Experimentation.getInstance().getPresetParameters().get(VisualDistractorParameter.class);
        if(vdp != null){
            visualDistractor_checkbox.setSelected(true);
            this.changePanelSelection(visualDist_panel, true);
            intervalTime_formattedTextField.setText(Integer.toString(vdp.getIntervalDuration()));
            durationTime_formattedTextField.setText(Integer.toString(vdp.getDisplayTime()));
            numberOfAppearance_formattedTextField.setText(Integer.toString(vdp.getNumberOfAppearance()));
            this.setSelectedPosition(visualDistPos_buttonGroup, vdp.getParameterPositionInTrial());
            this.loadColor(vdp.getColors());
            this.loadShapes(vdp.getShapes());
            m_controller.setVisualDistractorCache(vdp);
        }
        
        TactileDistractorParameter tdp = (TactileDistractorParameter) Experimentation.getInstance().getPresetParameters().get(TactileDistractorParameter.class);
        if(tdp != null){
            tactileDistractor_checkBox.setSelected(true);
            this.changePanelSelection(tactileDist_panel, true);
            shortVibDuration_formattedTextField.setText(Integer.toString(tdp.getShortVibrationDuration()));
            longVibDuration_formattedTextField.setText(Integer.toString(tdp.getLongVibrationDuration()));
            vibDownTime_formattedTextField.setText(Integer.toString(tdp.getIntervalDuration()));
            numberOfVib_formattedTextField.setText(Integer.toString(tdp.getNumberOfVibration()));
            this.setSelectedPosition(tactileDistPos_buttonGroup, tdp.getParameterPositionInTrial());
            m_controller.setTactileDistractorCache(tdp);
        }
        
        NoiseParameter np = (NoiseParameter) Experimentation.getInstance().getPresetParameters().get(NoiseParameter.class);
        if(np != null){
            noise_checkbox.setSelected(true);
            noiseChoice_comboPanel.setEnabled(true);
            for(int i = 0; i < noiseChoice_comboPanel.getItemCount(); i++){
                if(noiseChoice_comboPanel.getItemAt(i).equals(np.getNoiseFilename())){
                    noiseChoice_comboPanel.setSelectedIndex(i);
                }
            }
            m_controller.setNoiseParameterCache(np);
        }
        else{
            noise_checkbox.doClick();
        }
        
        if(Experimentation.getInstance().getWordsPerTrial() > 0){
            wordsPerTrial_spinner.setValue(Experimentation.getInstance().getWordsPerTrial());
        }
        
        if(Experimentation.getInstance().getDefaultNumberOfTrials() > 0){
            trialsPerBlock_spinner.setValue(Experimentation.getInstance().getDefaultNumberOfTrials());
        }
    }
    
    private void changePanelSelection(java.awt.Container aContainer, boolean isSelected){
        aContainer.setEnabled(isSelected);
        for(Component component : aContainer.getComponents()){
            if(component instanceof java.awt.Container){
                this.changePanelSelection(((java.awt.Container)component), isSelected);
            }
            else{
                aContainer.setEnabled(isSelected);
            }
        }
    }
    
    private void setSelectedPosition(ButtonGroup buttonGroup, EParameterPosition position){
        AbstractButton[] buttons = new AbstractButton[buttonGroup.getButtonCount()];
        Enumeration<AbstractButton> e = buttonGroup.getElements();
        int i = 0;
        while(e.hasMoreElements()){
            buttons[i] = e.nextElement();
            i++;
        }
        for(int j = 0; j < buttons.length; j++){
            if(EParameterPosition.values()[j].equals(position)){
                buttons[j].setSelected(true);
            }
        }
    }
    
    private void loadNoiseComboPanel(){
        ArrayList<String> filenames = FolderReader.getFilenamesOfSupportedFormats(Global.NOISE_FOLDER_PATH, Global.SUPPORT_AUDIO_FORMAT);
        for(String filename:filenames){
             noiseChoice_comboPanel.addItem(filename);
        }
    }
    
    private void loadColor(ArrayList<Color> colors){
        for(Color color:colors){
            if(color.equals(Color.RED)){
                red_checkBox.setSelected(true);
            }
            if(color.equals(Color.BLUE)){
                blue_checkBox.setSelected(true);
            }
            if(color.equals(Color.GREEN)){
                green_checkBox.setSelected(true);
            }
            if(color.equals(Color.YELLOW)){
                yellow_checkBox.setSelected(true);
            }
            if(color.equals(Color.CYAN)){
                cyan_checkBox.setSelected(true);
            }
        }
    }
    
    private void loadShapes(ArrayList<AbstractShape> shapes){
        for(AbstractShape shape:shapes){
            if(shape.getClass().equals(Circle.class)){
                circle_checkBox.setSelected(true);
            }
            if(shape.getClass().equals(Diamond.class)){
                diamond_checkBox.setSelected(true);
            }
            if(shape.getClass().equals(Hexagon.class)){
                hexagon_checkBox.setSelected(true);
            }
            if(shape.getClass().equals(Octogon.class)){
                octogone_checkBox.setSelected(true);
            }
            if(shape.getClass().equals(Pentagon.class)){
                pentagon_checkbox.setSelected(true);
            }
            if(shape.getClass().equals(Pentagram.class)){
                pentagram_checkbox.setSelected(true);
            }
            if(shape.getClass().equals(Rectangle.class)){
                rectangle_checkbox.setSelected(true);
            }
            if(shape.getClass().equals(Trapeze.class)){
                trapeze_checkBox.setSelected(true);
            }
            if(shape.getClass().equals(Triangle.class)){
                triangle_checkbox.setSelected(true);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tactileDistPos_buttonGroup = new javax.swing.ButtonGroup();
        visualDistPos_buttonGroup = new javax.swing.ButtonGroup();
        ok_button = new javax.swing.JButton();
        cancel_button = new javax.swing.JButton();
        tactileDist_panel = new javax.swing.JPanel();
        vibDownTime_label = new javax.swing.JLabel();
        longVibDuration_label = new javax.swing.JLabel();
        shortVibDuration_label = new javax.swing.JLabel();
        numberOfVib_label = new javax.swing.JLabel();
        shortVibMillisec_label = new javax.swing.JLabel();
        longVibMillisec_label = new javax.swing.JLabel();
        vibDownTimeMillisec_label = new javax.swing.JLabel();
        tactDistPos_label = new javax.swing.JLabel();
        tactDistBefore_radioButton = new javax.swing.JRadioButton();
        tactDistAfter_radioButton = new javax.swing.JRadioButton();
        shortVibDuration_formattedTextField = new javax.swing.JFormattedTextField();
        longVibDuration_formattedTextField = new javax.swing.JFormattedTextField();
        vibDownTime_formattedTextField = new javax.swing.JFormattedTextField();
        numberOfVib_formattedTextField = new javax.swing.JFormattedTextField();
        tactileDist_separator = new javax.swing.JSeparator();
        visualDist_panel = new javax.swing.JPanel();
        shapesOption_scrollpane = new javax.swing.JScrollPane();
        shapesOption_panel = new javax.swing.JPanel();
        circle_checkBox = new javax.swing.JCheckBox();
        rectangle_checkbox = new javax.swing.JCheckBox();
        triangle_checkbox = new javax.swing.JCheckBox();
        diamond_checkBox = new javax.swing.JCheckBox();
        pentagon_checkbox = new javax.swing.JCheckBox();
        hexagon_checkBox = new javax.swing.JCheckBox();
        octogone_checkBox = new javax.swing.JCheckBox();
        trapeze_checkBox = new javax.swing.JCheckBox();
        pentagram_checkbox = new javax.swing.JCheckBox();
        shapes_label = new javax.swing.JLabel();
        color_label = new javax.swing.JLabel();
        colorOption_scrollpane = new javax.swing.JScrollPane();
        colorOption_panel = new javax.swing.JPanel();
        red_checkBox = new javax.swing.JCheckBox();
        green_checkBox = new javax.swing.JCheckBox();
        blue_checkBox = new javax.swing.JCheckBox();
        yellow_checkBox = new javax.swing.JCheckBox();
        magenta_checkBox = new javax.swing.JCheckBox();
        cyan_checkBox = new javax.swing.JCheckBox();
        otherparams_panel = new javax.swing.JPanel();
        durationTime_labe = new javax.swing.JLabel();
        durationTimeMillisec_label = new javax.swing.JLabel();
        intervalTime_label = new javax.swing.JLabel();
        numberOfappearance_label = new javax.swing.JLabel();
        intervalTimeMillisec_label = new javax.swing.JLabel();
        durationTime_formattedTextField = new javax.swing.JFormattedTextField();
        intervalTime_formattedTextField = new javax.swing.JFormattedTextField();
        numberOfAppearance_formattedTextField = new javax.swing.JFormattedTextField();
        visDistPos_label = new javax.swing.JLabel();
        visDistBefore_radioButton = new javax.swing.JRadioButton();
        visDistAfter_radioButton = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        miscParam_panel = new javax.swing.JPanel();
        noise_checkbox = new javax.swing.JCheckBox();
        noiseChoice_comboPanel = new javax.swing.JComboBox();
        otherParam_label = new javax.swing.JLabel();
        trialsPerBlock_label = new javax.swing.JLabel();
        trialsPerBlock_spinner = new javax.swing.JSpinner();
        wordsPerTrial_label = new javax.swing.JLabel();
        wordsPerTrial_spinner = new javax.swing.JSpinner();
        tactileDistractor_checkBox = new javax.swing.JCheckBox();
        visualDistractor_checkbox = new javax.swing.JCheckBox();
        saveAsDefault_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Préselection des paramètres d'expérience");

        ok_button.setText("OK");
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_buttonActionPerformed(evt);
            }
        });

        cancel_button.setText("Annuler");
        cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_buttonActionPerformed(evt);
            }
        });

        vibDownTime_label.setText("Intervalle:");
        vibDownTime_label.setEnabled(false);

        longVibDuration_label.setText("Longue vibration:");
        longVibDuration_label.setEnabled(false);

        shortVibDuration_label.setText("Courte vibration:");
        shortVibDuration_label.setEnabled(false);

        numberOfVib_label.setText("Nbr. vibrations:");
        numberOfVib_label.setEnabled(false);

        shortVibMillisec_label.setText("ms");
        shortVibMillisec_label.setEnabled(false);

        longVibMillisec_label.setText("ms");
        longVibMillisec_label.setEnabled(false);

        vibDownTimeMillisec_label.setText("ms");
        vibDownTimeMillisec_label.setEnabled(false);

        tactDistPos_label.setText("Positionnement:");
        tactDistPos_label.setEnabled(false);

        tactileDistPos_buttonGroup.add(tactDistBefore_radioButton);
        tactDistBefore_radioButton.setSelected(true);
        tactDistBefore_radioButton.setText("Début");
        tactDistBefore_radioButton.setActionCommand("BEFORE");
        tactDistBefore_radioButton.setEnabled(false);
        tactDistBefore_radioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tactDistBefore_radioButtonActionPerformed(evt);
            }
        });

        tactileDistPos_buttonGroup.add(tactDistAfter_radioButton);
        tactDistAfter_radioButton.setText("Fin");
        tactDistAfter_radioButton.setActionCommand("AFTER");
        tactDistAfter_radioButton.setEnabled(false);
        tactDistAfter_radioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tactDistAfter_radioButtonActionPerformed(evt);
            }
        });

        shortVibDuration_formattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        shortVibDuration_formattedTextField.setEnabled(false);
        shortVibDuration_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                shortVibDuration_formattedTextFieldFocusLost(evt);
            }
        });

        longVibDuration_formattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        longVibDuration_formattedTextField.setEnabled(false);
        longVibDuration_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                longVibDuration_formattedTextFieldFocusLost(evt);
            }
        });

        vibDownTime_formattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        vibDownTime_formattedTextField.setEnabled(false);
        vibDownTime_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                vibDownTime_formattedTextFieldFocusLost(evt);
            }
        });

        numberOfVib_formattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        numberOfVib_formattedTextField.setEnabled(false);
        numberOfVib_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberOfVib_formattedTextFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout tactileDist_panelLayout = new javax.swing.GroupLayout(tactileDist_panel);
        tactileDist_panel.setLayout(tactileDist_panelLayout);
        tactileDist_panelLayout.setHorizontalGroup(
            tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tactileDist_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tactDistPos_label)
                    .addGroup(tactileDist_panelLayout.createSequentialGroup()
                        .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tactileDist_panelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(tactDistBefore_radioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tactDistAfter_radioButton))
                            .addGroup(tactileDist_panelLayout.createSequentialGroup()
                                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(shortVibDuration_label)
                                    .addComponent(longVibDuration_label))
                                .addGap(10, 10, 10)
                                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shortVibDuration_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(longVibDuration_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shortVibMillisec_label)
                                    .addComponent(longVibMillisec_label))))
                        .addGap(32, 32, 32)
                        .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(numberOfVib_label)
                            .addComponent(vibDownTime_label))
                        .addGap(18, 18, 18)
                        .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tactileDist_panelLayout.createSequentialGroup()
                                .addComponent(vibDownTime_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vibDownTimeMillisec_label))
                            .addComponent(numberOfVib_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tactileDist_panelLayout.setVerticalGroup(
            tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tactileDist_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tactDistPos_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tactDistBefore_radioButton)
                    .addComponent(tactDistAfter_radioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
                .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tactileDist_panelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numberOfVib_label)
                            .addComponent(longVibDuration_label)
                            .addComponent(longVibMillisec_label)
                            .addComponent(longVibDuration_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numberOfVib_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vibDownTimeMillisec_label)
                        .addComponent(vibDownTime_label)
                        .addComponent(vibDownTime_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tactileDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(shortVibDuration_label)
                        .addComponent(shortVibMillisec_label)
                        .addComponent(shortVibDuration_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        visualDist_panel.setEnabled(false);

        shapesOption_scrollpane.setEnabled(false);

        shapesOption_panel.setEnabled(false);
        shapesOption_panel.setPreferredSize(new java.awt.Dimension(1, 240));

        circle_checkBox.setText("Cercle");
        circle_checkBox.setToolTipText("");
        circle_checkBox.setAutoscrolls(true);
        circle_checkBox.setEnabled(false);
        circle_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                circle_checkBoxActionPerformed(evt);
            }
        });

        rectangle_checkbox.setText("Rectangle");
        rectangle_checkbox.setEnabled(false);
        rectangle_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectangle_checkboxActionPerformed(evt);
            }
        });

        triangle_checkbox.setText("Triangle");
        triangle_checkbox.setEnabled(false);
        triangle_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triangle_checkboxActionPerformed(evt);
            }
        });

        diamond_checkBox.setText("Diamant");
        diamond_checkBox.setEnabled(false);
        diamond_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diamond_checkBoxActionPerformed(evt);
            }
        });

        pentagon_checkbox.setText("Pentagone");
        pentagon_checkbox.setEnabled(false);
        pentagon_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pentagon_checkboxActionPerformed(evt);
            }
        });

        hexagon_checkBox.setText("Hexagone");
        hexagon_checkBox.setEnabled(false);
        hexagon_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexagon_checkBoxActionPerformed(evt);
            }
        });

        octogone_checkBox.setText("Octogone");
        octogone_checkBox.setEnabled(false);
        octogone_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                octogone_checkBoxActionPerformed(evt);
            }
        });

        trapeze_checkBox.setText("Trapeze");
        trapeze_checkBox.setEnabled(false);
        trapeze_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trapeze_checkBoxActionPerformed(evt);
            }
        });

        pentagram_checkbox.setText("Pentagramme");
        pentagram_checkbox.setEnabled(false);
        pentagram_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pentagram_checkboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout shapesOption_panelLayout = new javax.swing.GroupLayout(shapesOption_panel);
        shapesOption_panel.setLayout(shapesOption_panelLayout);
        shapesOption_panelLayout.setHorizontalGroup(
            shapesOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shapesOption_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shapesOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(circle_checkBox)
                    .addComponent(rectangle_checkbox)
                    .addComponent(triangle_checkbox)
                    .addComponent(diamond_checkBox)
                    .addComponent(pentagon_checkbox)
                    .addComponent(hexagon_checkBox)
                    .addComponent(octogone_checkBox)
                    .addComponent(trapeze_checkBox)
                    .addComponent(pentagram_checkbox))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        shapesOption_panelLayout.setVerticalGroup(
            shapesOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shapesOption_panelLayout.createSequentialGroup()
                .addComponent(circle_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rectangle_checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(triangle_checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diamond_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pentagon_checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hexagon_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(octogone_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trapeze_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pentagram_checkbox)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        shapesOption_scrollpane.setViewportView(shapesOption_panel);

        shapes_label.setText("Formes");
        shapes_label.setEnabled(false);

        color_label.setText("Couleurs");
        color_label.setEnabled(false);

        colorOption_scrollpane.setEnabled(false);
        colorOption_scrollpane.setPreferredSize(new java.awt.Dimension(3, 130));

        colorOption_panel.setEnabled(false);
        colorOption_panel.setPreferredSize(new java.awt.Dimension(1, 150));
        colorOption_panel.setRequestFocusEnabled(false);

        red_checkBox.setText("Rouge");
        red_checkBox.setEnabled(false);
        red_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                red_checkBoxActionPerformed(evt);
            }
        });

        green_checkBox.setText("Vert");
        green_checkBox.setEnabled(false);
        green_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                green_checkBoxActionPerformed(evt);
            }
        });

        blue_checkBox.setText("Bleu");
        blue_checkBox.setEnabled(false);
        blue_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blue_checkBoxActionPerformed(evt);
            }
        });

        yellow_checkBox.setText("Jaune");
        yellow_checkBox.setEnabled(false);
        yellow_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellow_checkBoxActionPerformed(evt);
            }
        });

        magenta_checkBox.setText("Magenta");
        magenta_checkBox.setActionCommand("magenta");
        magenta_checkBox.setEnabled(false);
        magenta_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                magenta_checkBoxActionPerformed(evt);
            }
        });

        cyan_checkBox.setText("Cyan");
        cyan_checkBox.setEnabled(false);
        cyan_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cyan_checkBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout colorOption_panelLayout = new javax.swing.GroupLayout(colorOption_panel);
        colorOption_panel.setLayout(colorOption_panelLayout);
        colorOption_panelLayout.setHorizontalGroup(
            colorOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorOption_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(red_checkBox)
                    .addComponent(green_checkBox)
                    .addComponent(blue_checkBox)
                    .addComponent(yellow_checkBox)
                    .addComponent(magenta_checkBox)
                    .addComponent(cyan_checkBox))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        colorOption_panelLayout.setVerticalGroup(
            colorOption_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorOption_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(red_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(green_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blue_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yellow_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(magenta_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cyan_checkBox))
        );

        colorOption_scrollpane.setViewportView(colorOption_panel);

        otherparams_panel.setEnabled(false);

        durationTime_labe.setText("Durée:");
        durationTime_labe.setEnabled(false);

        durationTimeMillisec_label.setText("ms");
        durationTimeMillisec_label.setEnabled(false);

        intervalTime_label.setText("Intervalle:");
        intervalTime_label.setEnabled(false);

        numberOfappearance_label.setText("Nbr. apparitions:");
        numberOfappearance_label.setEnabled(false);

        intervalTimeMillisec_label.setText("ms");
        intervalTimeMillisec_label.setEnabled(false);

        durationTime_formattedTextField.setEnabled(false);
        durationTime_formattedTextField.setFocusTraversalPolicyProvider(true);
        durationTime_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                durationTime_formattedTextFieldFocusLost(evt);
            }
        });

        intervalTime_formattedTextField.setEnabled(false);
        intervalTime_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                intervalTime_formattedTextFieldFocusLost(evt);
            }
        });

        numberOfAppearance_formattedTextField.setEnabled(false);
        numberOfAppearance_formattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberOfAppearance_formattedTextFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout otherparams_panelLayout = new javax.swing.GroupLayout(otherparams_panel);
        otherparams_panel.setLayout(otherparams_panelLayout);
        otherparams_panelLayout.setHorizontalGroup(
            otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherparams_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numberOfappearance_label)
                    .addComponent(intervalTime_label)
                    .addComponent(durationTime_labe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(durationTime_formattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(intervalTime_formattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                        .addComponent(numberOfAppearance_formattedTextField)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(intervalTimeMillisec_label)
                    .addComponent(durationTimeMillisec_label, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        otherparams_panelLayout.setVerticalGroup(
            otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherparams_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durationTime_labe)
                    .addComponent(durationTimeMillisec_label)
                    .addComponent(durationTime_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intervalTime_label)
                    .addComponent(intervalTimeMillisec_label)
                    .addComponent(intervalTime_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(otherparams_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberOfappearance_label)
                    .addComponent(numberOfAppearance_formattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        visDistPos_label.setText("Positionnement:");
        visDistPos_label.setEnabled(false);

        visualDistPos_buttonGroup.add(visDistBefore_radioButton);
        visDistBefore_radioButton.setSelected(true);
        visDistBefore_radioButton.setText("Début");
        visDistBefore_radioButton.setActionCommand("BEFORE");
        visDistBefore_radioButton.setEnabled(false);
        visDistBefore_radioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visDistBefore_radioButtonActionPerformed(evt);
            }
        });

        visualDistPos_buttonGroup.add(visDistAfter_radioButton);
        visDistAfter_radioButton.setText("Fin");
        visDistAfter_radioButton.setActionCommand("AFTER");
        visDistAfter_radioButton.setEnabled(false);
        visDistAfter_radioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visDistAfter_radioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout visualDist_panelLayout = new javax.swing.GroupLayout(visualDist_panel);
        visualDist_panel.setLayout(visualDist_panelLayout);
        visualDist_panelLayout.setHorizontalGroup(
            visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visualDist_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(visualDist_panelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(visDistBefore_radioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(visDistAfter_radioButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(visualDist_panelLayout.createSequentialGroup()
                        .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(visualDist_panelLayout.createSequentialGroup()
                                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shapes_label)
                                    .addComponent(shapesOption_scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(visualDist_panelLayout.createSequentialGroup()
                                        .addComponent(colorOption_scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(otherparams_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(color_label)))
                            .addComponent(visDistPos_label))
                        .addGap(14, 14, 14))))
        );
        visualDist_panelLayout.setVerticalGroup(
            visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, visualDist_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visDistPos_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(visDistBefore_radioButton)
                    .addComponent(visDistAfter_radioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(color_label)
                    .addComponent(shapes_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(otherparams_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(visualDist_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(colorOption_scrollpane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(shapesOption_scrollpane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );

        noise_checkbox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        noise_checkbox.setLabel("Bruit");
        noise_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noise_checkboxActionPerformed(evt);
            }
        });

        noiseChoice_comboPanel.setEnabled(false);
        noiseChoice_comboPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noiseChoice_comboPanelActionPerformed(evt);
            }
        });

        otherParam_label.setText("Autres paramètres");

        trialsPerBlock_label.setText("Essais par bloc");

        trialsPerBlock_spinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        wordsPerTrial_label.setText("Nombre de mots");

        wordsPerTrial_spinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(7), Integer.valueOf(1)));
        wordsPerTrial_spinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        wordsPerTrial_spinner.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout miscParam_panelLayout = new javax.swing.GroupLayout(miscParam_panel);
        miscParam_panel.setLayout(miscParam_panelLayout);
        miscParam_panelLayout.setHorizontalGroup(
            miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miscParam_panelLayout.createSequentialGroup()
                .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(miscParam_panelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noise_checkbox)
                            .addGroup(miscParam_panelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(noiseChoice_comboPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(miscParam_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(otherParam_label)))
                .addGap(131, 131, 131)
                .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trialsPerBlock_label)
                    .addComponent(wordsPerTrial_label))
                .addGap(29, 29, 29)
                .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trialsPerBlock_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wordsPerTrial_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        miscParam_panelLayout.setVerticalGroup(
            miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(miscParam_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(otherParam_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noise_checkbox)
                    .addComponent(trialsPerBlock_label)
                    .addComponent(trialsPerBlock_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(miscParam_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(wordsPerTrial_label)
                        .addComponent(wordsPerTrial_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(noiseChoice_comboPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tactileDistractor_checkBox.setText("Distracteur tactile");
        tactileDistractor_checkBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        tactileDistractor_checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tactileDistractor_checkBoxActionPerformed(evt);
            }
        });

        visualDistractor_checkbox.setText("Distracteur visuel");
        visualDistractor_checkbox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        visualDistractor_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualDistractor_checkboxActionPerformed(evt);
            }
        });

        saveAsDefault_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/icons/save_icon&16.png"))); // NOI18N
        saveAsDefault_button.setText(" comme défaut");
        saveAsDefault_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsDefault_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tactileDist_separator)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveAsDefault_button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancel_button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ok_button, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(miscParam_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(tactileDistractor_checkBox)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(visualDist_panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(9, 9, 9))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(visualDistractor_checkbox)
                            .addComponent(tactileDist_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tactileDistractor_checkBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tactileDist_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tactileDist_separator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(visualDistractor_checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(visualDist_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(miscParam_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok_button)
                    .addComponent(cancel_button)
                    .addComponent(saveAsDefault_button))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tactileDistractor_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tactileDistractor_checkBoxActionPerformed
        this.changePanelSelection(tactileDist_panel, tactileDistractor_checkBox.isSelected());
    }//GEN-LAST:event_tactileDistractor_checkBoxActionPerformed

    private void visualDistractor_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualDistractor_checkboxActionPerformed
        this.changePanelSelection(visualDist_panel, visualDistractor_checkbox.isSelected());
    }//GEN-LAST:event_visualDistractor_checkboxActionPerformed

    private void noise_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noise_checkboxActionPerformed
        noiseChoice_comboPanel.setEnabled(noise_checkbox.isSelected());
        if(noise_checkbox.isSelected()){
            int lol = noiseChoice_comboPanel.getItemCount();
            m_controller.setBackgroundNoise(noiseChoice_comboPanel.getSelectedItem().toString());
        }
    }//GEN-LAST:event_noise_checkboxActionPerformed

    private void shortVibDuration_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_shortVibDuration_formattedTextFieldFocusLost
        boolean success = m_controller.setShortVibDuration(shortVibDuration_formattedTextField.getText());
        if(!success){   
            shortVibDuration_formattedTextField.setText("");
        }
    }//GEN-LAST:event_shortVibDuration_formattedTextFieldFocusLost

    private void longVibDuration_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_longVibDuration_formattedTextFieldFocusLost
        boolean success = m_controller.setLongVibDuration(longVibDuration_formattedTextField.getText());
        if(!success){   
            longVibDuration_formattedTextField.setText("");
        }
    }//GEN-LAST:event_longVibDuration_formattedTextFieldFocusLost

    private void vibDownTime_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vibDownTime_formattedTextFieldFocusLost
        boolean success = m_controller.setTactileDistractorIntervalDuration(vibDownTime_formattedTextField.getText());
        if(!success){   
            vibDownTime_formattedTextField.setText("");
        }
    }//GEN-LAST:event_vibDownTime_formattedTextFieldFocusLost

    private void numberOfVib_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numberOfVib_formattedTextFieldFocusLost
        boolean success = m_controller.setNumberOfVibrations(numberOfVib_formattedTextField.getText());
        if(!success){   
            numberOfVib_formattedTextField.setText("");
        }
    }//GEN-LAST:event_numberOfVib_formattedTextFieldFocusLost

    private void durationTime_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_durationTime_formattedTextFieldFocusLost
        boolean success = m_controller.setDisplayTime(durationTime_formattedTextField.getText());
        if(!success){   
            durationTime_formattedTextField.setText("");
        }
    }//GEN-LAST:event_durationTime_formattedTextFieldFocusLost

    private void intervalTime_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_intervalTime_formattedTextFieldFocusLost
        boolean success = m_controller.setVisualDistractorIntervalDuration(intervalTime_formattedTextField.getText());
        if(!success){   
            intervalTime_formattedTextField.setText("");
        }
    }//GEN-LAST:event_intervalTime_formattedTextFieldFocusLost

    private void numberOfAppearance_formattedTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numberOfAppearance_formattedTextFieldFocusLost
        boolean success = m_controller.setNumberOfAppearance(numberOfAppearance_formattedTextField.getText());
        if(!success){   
            numberOfAppearance_formattedTextField.setText("");
        }
    }//GEN-LAST:event_numberOfAppearance_formattedTextFieldFocusLost

    private void ok_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_buttonActionPerformed
        confirmChoices();
        this.dispose();
    }//GEN-LAST:event_ok_buttonActionPerformed

    private void cancel_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_buttonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancel_buttonActionPerformed

    private void tactDistBefore_radioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tactDistBefore_radioButtonActionPerformed
        m_controller.setTactileParameterPositionInTrial(EParameterPosition.BEFORE);
    }//GEN-LAST:event_tactDistBefore_radioButtonActionPerformed

    private void tactDistAfter_radioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tactDistAfter_radioButtonActionPerformed
        m_controller.setTactileParameterPositionInTrial(EParameterPosition.AFTER);
    }//GEN-LAST:event_tactDistAfter_radioButtonActionPerformed

    private void visDistBefore_radioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visDistBefore_radioButtonActionPerformed
        m_controller.setVisualParameterPositionInTrial(EParameterPosition.BEFORE);
    }//GEN-LAST:event_visDistBefore_radioButtonActionPerformed

    private void visDistAfter_radioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visDistAfter_radioButtonActionPerformed
        m_controller.setVisualParameterPositionInTrial(EParameterPosition.AFTER);
    }//GEN-LAST:event_visDistAfter_radioButtonActionPerformed

    private void red_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_red_checkBoxActionPerformed
        if(red_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.RED);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.RED);
        }
    }//GEN-LAST:event_red_checkBoxActionPerformed

    private void green_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_green_checkBoxActionPerformed
        if(green_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.GREEN);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.GREEN);
        }
    }//GEN-LAST:event_green_checkBoxActionPerformed

    private void blue_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blue_checkBoxActionPerformed
        if(blue_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.BLUE);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.BLUE);
        }
    }//GEN-LAST:event_blue_checkBoxActionPerformed

    private void yellow_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yellow_checkBoxActionPerformed
        if(yellow_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.YELLOW);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.YELLOW);
        }
    }//GEN-LAST:event_yellow_checkBoxActionPerformed

    private void magenta_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_magenta_checkBoxActionPerformed
        if(magenta_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.MAGENTA);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.MAGENTA);
        }
    }//GEN-LAST:event_magenta_checkBoxActionPerformed

    private void cyan_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cyan_checkBoxActionPerformed
        if(cyan_checkBox.isSelected()){
            m_controller.addColorToVisualDitractor(Color.CYAN);
        }
        else{
            m_controller.removeColorFromVisualDitractor(Color.CYAN);
        }
    }//GEN-LAST:event_cyan_checkBoxActionPerformed

    private void noiseChoice_comboPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noiseChoice_comboPanelActionPerformed
        m_controller.setBackgroundNoise(noiseChoice_comboPanel.getSelectedItem().toString());
    }//GEN-LAST:event_noiseChoice_comboPanelActionPerformed

    private void pentagram_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pentagram_checkboxActionPerformed
        if(pentagram_checkbox.isSelected()){
            m_controller.addShape(new Pentagram());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Pentagram.class);
        }
    }//GEN-LAST:event_pentagram_checkboxActionPerformed

    private void trapeze_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trapeze_checkBoxActionPerformed
        if(trapeze_checkBox.isSelected()){
            m_controller.addShape(new Trapeze());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Trapeze.class);
        }
    }//GEN-LAST:event_trapeze_checkBoxActionPerformed

    private void octogone_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octogone_checkBoxActionPerformed
        if(octogone_checkBox.isSelected()){
            m_controller.addShape(new Octogon());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Octogon.class);
        }
    }//GEN-LAST:event_octogone_checkBoxActionPerformed

    private void hexagon_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexagon_checkBoxActionPerformed
        if(hexagon_checkBox.isSelected()){
            m_controller.addShape(new Hexagon());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Hexagon.class);
        }
    }//GEN-LAST:event_hexagon_checkBoxActionPerformed

    private void pentagon_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pentagon_checkboxActionPerformed
        if(pentagon_checkbox.isSelected()){
            m_controller.addShape(new Pentagon());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Pentagon.class);
        }
    }//GEN-LAST:event_pentagon_checkboxActionPerformed

    private void diamond_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diamond_checkBoxActionPerformed
        if(diamond_checkBox.isSelected()){
            m_controller.addShape(new Diamond());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Diamond.class);
        }
    }//GEN-LAST:event_diamond_checkBoxActionPerformed

    private void triangle_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triangle_checkboxActionPerformed
        if(triangle_checkbox.isSelected()){
            m_controller.addShape(new Triangle());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Triangle.class);
        }
    }//GEN-LAST:event_triangle_checkboxActionPerformed

    private void rectangle_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectangle_checkboxActionPerformed
        if(rectangle_checkbox.isSelected()){
            m_controller.addShape(new Rectangle());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Rectangle.class);
        }
    }//GEN-LAST:event_rectangle_checkboxActionPerformed

    private void circle_checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_circle_checkBoxActionPerformed
        if(circle_checkBox.isSelected()){
            m_controller.addShape(new Circle());
        }
        else{
            m_controller.removeShapeFromVisualDistractor(Circle.class);
        }
    }//GEN-LAST:event_circle_checkBoxActionPerformed

    private void saveAsDefault_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsDefault_buttonActionPerformed
        confirmChoices();
        try {
            m_controller.saveDefault();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParameterView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParameterView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveAsDefault_buttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox blue_checkBox;
    private javax.swing.JButton cancel_button;
    private javax.swing.JCheckBox circle_checkBox;
    private javax.swing.JPanel colorOption_panel;
    private javax.swing.JScrollPane colorOption_scrollpane;
    private javax.swing.JLabel color_label;
    private javax.swing.JCheckBox cyan_checkBox;
    private javax.swing.JCheckBox diamond_checkBox;
    private javax.swing.JLabel durationTimeMillisec_label;
    private javax.swing.JFormattedTextField durationTime_formattedTextField;
    private javax.swing.JLabel durationTime_labe;
    private javax.swing.JCheckBox green_checkBox;
    private javax.swing.JCheckBox hexagon_checkBox;
    private javax.swing.JLabel intervalTimeMillisec_label;
    private javax.swing.JFormattedTextField intervalTime_formattedTextField;
    private javax.swing.JLabel intervalTime_label;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JFormattedTextField longVibDuration_formattedTextField;
    private javax.swing.JLabel longVibDuration_label;
    private javax.swing.JLabel longVibMillisec_label;
    private javax.swing.JCheckBox magenta_checkBox;
    private javax.swing.JPanel miscParam_panel;
    private javax.swing.JComboBox noiseChoice_comboPanel;
    private javax.swing.JCheckBox noise_checkbox;
    private javax.swing.JFormattedTextField numberOfAppearance_formattedTextField;
    private javax.swing.JFormattedTextField numberOfVib_formattedTextField;
    private javax.swing.JLabel numberOfVib_label;
    private javax.swing.JLabel numberOfappearance_label;
    private javax.swing.JCheckBox octogone_checkBox;
    private javax.swing.JButton ok_button;
    private javax.swing.JLabel otherParam_label;
    private javax.swing.JPanel otherparams_panel;
    private javax.swing.JCheckBox pentagon_checkbox;
    private javax.swing.JCheckBox pentagram_checkbox;
    private javax.swing.JCheckBox rectangle_checkbox;
    private javax.swing.JCheckBox red_checkBox;
    private javax.swing.JButton saveAsDefault_button;
    private javax.swing.JPanel shapesOption_panel;
    private javax.swing.JScrollPane shapesOption_scrollpane;
    private javax.swing.JLabel shapes_label;
    private javax.swing.JFormattedTextField shortVibDuration_formattedTextField;
    private javax.swing.JLabel shortVibDuration_label;
    private javax.swing.JLabel shortVibMillisec_label;
    private javax.swing.JRadioButton tactDistAfter_radioButton;
    private javax.swing.JRadioButton tactDistBefore_radioButton;
    private javax.swing.JLabel tactDistPos_label;
    private javax.swing.ButtonGroup tactileDistPos_buttonGroup;
    private javax.swing.JPanel tactileDist_panel;
    private javax.swing.JSeparator tactileDist_separator;
    private javax.swing.JCheckBox tactileDistractor_checkBox;
    private javax.swing.JCheckBox trapeze_checkBox;
    private javax.swing.JLabel trialsPerBlock_label;
    private javax.swing.JSpinner trialsPerBlock_spinner;
    private javax.swing.JCheckBox triangle_checkbox;
    private javax.swing.JLabel vibDownTimeMillisec_label;
    private javax.swing.JFormattedTextField vibDownTime_formattedTextField;
    private javax.swing.JLabel vibDownTime_label;
    private javax.swing.JRadioButton visDistAfter_radioButton;
    private javax.swing.JRadioButton visDistBefore_radioButton;
    private javax.swing.JLabel visDistPos_label;
    private javax.swing.ButtonGroup visualDistPos_buttonGroup;
    private javax.swing.JPanel visualDist_panel;
    private javax.swing.JCheckBox visualDistractor_checkbox;
    private javax.swing.JLabel wordsPerTrial_label;
    private javax.swing.JSpinner wordsPerTrial_spinner;
    private javax.swing.JCheckBox yellow_checkBox;
    // End of variables declaration//GEN-END:variables

    /**
     * Push cached parameters or delete the one unchecked.
     */
    private void confirmChoices() {
        if(tactileDistractor_checkBox.isSelected()){
            m_controller.pushTactileDistractorParameter();
        }
        else{
            m_controller.removeParameterFromExperiment(TactileDistractorParameter.class);
        }
        
        if(visualDistractor_checkbox.isSelected()){
            m_controller.pushVisualDistractorParameter();
        }
        else{
            m_controller.removeParameterFromExperiment(VisualDistractorParameter.class);
        }
        
        if(noise_checkbox.isSelected()){
            m_controller.pushNoiseParameter();
        }
        else{
            m_controller.removeParameterFromExperiment(NoiseParameter.class);
        }
        m_controller.setWordsPerTrial((int)wordsPerTrial_spinner.getValue());
        m_controller.setTrialsPerBlock((int)trialsPerBlock_spinner.getValue());        
    }
}
