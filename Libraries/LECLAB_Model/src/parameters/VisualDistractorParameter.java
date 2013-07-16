package parameters;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import parameters.geometry.AbstractShape;
import parameters.geometry.Circle;
/**
 * This is a children class of Parameter.
 * It encapsulate the behaviour of the visual distractor during  the experiment
 * The visual distractor displays numerous geometric shapes one at time. 
 * 
 * @see Parameter
 * 
 * @author Philippe de Sève, Jean-Etienne Salois
 */
public class VisualDistractorParameter extends Parameter{
    /**
     * List of the shapes which can possibly displayed
     */
    private ArrayList<AbstractShape>  m_shapes;
    
    /**
     * List of all the colors the shapes can have when displayed
     */
    private ArrayList<Color> m_colors;
    
    /**
     * The number of shape which will sequentially be displayed
     */
    private int m_numberOfAppearance;
    
    /**
     * The duration in milliseconds during which a shape will be displayed
     */
    private int m_displayTime;
    
    /**
     * The down time in milliseconds between the appearing of 2 shapes.
     */
    private int m_intervalDuration;
    
    /**
     * Default constructor. All attributes are empty or set to 0.
     */
    public VisualDistractorParameter(){
        super(EParameterPosition.BEFORE);
        m_shapes = new ArrayList<AbstractShape>();
        m_shapes.add(new Circle());
        m_colors = new ArrayList<Color>();
        m_colors.add(Color.red);
        m_numberOfAppearance = 0;
        m_displayTime = 0;
        m_intervalDuration = 0;
    }
    
    /**
     * Copy constructor.
     * @param distractor
     *          the VisualDistractorParameter object to copy
     */
    public VisualDistractorParameter(VisualDistractorParameter distractor){
        super(distractor.getParameterPositionInTrial());
        m_shapes = distractor.m_shapes;
        m_colors = distractor.m_colors;
        m_numberOfAppearance = distractor.m_numberOfAppearance;
        m_displayTime = distractor.m_displayTime;
        m_intervalDuration = distractor.m_intervalDuration;
    }
    
    /**
     * CLone redefinition.
     * @return the cloned object
     */
    @Override
    public VisualDistractorParameter clone(){
        return new VisualDistractorParameter(this);
    }
    
    
    /**
     * Setter for m_numberOfAppearance. It must be greater than 0 to be assigned.
     * @param numberOfAppearance 
     *          the new number of shapes to display
     * @return if the assignation was a success
     * @see #m_numberOfAppearance
     */
    public boolean setNumberOfAppearance(int numberOfAppearance){
        //TODO une exception instead of boolean return
        if(numberOfAppearance >= 0){
            m_numberOfAppearance = numberOfAppearance;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Setter for m_displayTime. It must be greater than 0 to be assigned.
     * @param displayTime 
     *          the new display duration 
     * @return if the assignation was a success
     * @see #m_displayTime
     */
    public boolean setDisplayTime(int displayTime){
        //TODO une exception instead of boolean return
        if(displayTime >= 0){   
            m_displayTime = displayTime;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Getter for m_intervalDuration.
     * @return interval duration
     * @see #m_intervalDuration
     */
    public int getIntervalDuration(){
        return m_intervalDuration;
    }
    
    /**
     * Getter for m_displayTime
     * @return display time
     * @see #m_displayTime
     */
    public int getDisplayTime(){
        return m_displayTime;
    }
    
    /**
     * Getter for m_numberOfAppearance
     * @return number of appearance
     * @see #m_numberOfAppearance
     */
    public int getNumberOfAppearance(){
        return m_numberOfAppearance;
    }
    
    /**
     * Getter for m_colors.
     * @return the array of colors
     * @see #m_colors
     */
    public ArrayList<Color> getColors(){
        return m_colors;
    }
    
    /**
     * Getter for m_shapes
     * @return the array of shapes
     * @see #m_shapes
     */
    public ArrayList<AbstractShape> getShapes(){
        return m_shapes;
    }
    
    /**
     * Setter for m_intervalDuration. It must be greater than 0 to be assigned.
     * @param intervalDuration 
     *          the new interval duration 
     * @return if the assignation was a success
     * @see #m_intervalDuration
     */
    public boolean setIntervalDuration(int intervalDuration){
        //TODO une exception instead of boolean return
        if(intervalDuration >= 0){
            m_intervalDuration = intervalDuration;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Adds a color to m_colors if the list does not contains the new color.
     * @param aColor 
     *          the color to add
     */
    public void addColor(Color aColor){
        if(!m_colors.contains(aColor)){
            m_colors.add(aColor);
        }
    }
    
    /**
     * Removes a color from m_colors 
     * @param aColor 
     *          the color to remove
     */
    public void removeColor(Color aColor){
        m_colors.remove(aColor);
    }
    
    /**
     * Adds a shape to m_shapes if the list does not contains the new shape.
     * @param aColor 
     *          the shape to add
     */
    public void addShape(AbstractShape aShape){
        if(!m_shapes.contains(aShape)){
            m_shapes.add(aShape);
        }
    }
    
    /**
     * Removes a shape from m_shape 
     * @param aShape 
     *          the shape to remove
     */
    public void removeShape(Class shapeClass){
        for(AbstractShape shape : m_shapes)
        {
            if(shape.getClass().equals(shapeClass)){
                m_shapes.remove(shape);
            }
        }  
    }
    
     /**
     * orderOfPlay implementation. Implements the behavior of the parameter 
     * when it is to be played during a block. The function uses a seed to 
     * generate a random sequence of stimulus to be played.
     * @param seed
     *          seed used to generate stimulus sequence 
     * @return the order, an AbstractShape array.
     */
    @Override
    public Object[] orderOfPlay(int seed) {
        Random rand = new Random(seed);
        AbstractShape[] order = new AbstractShape[m_numberOfAppearance];
        for(int i = 0; i < order.length; i++){
            AbstractShape randomShape = m_shapes.get(rand.nextInt(m_shapes.size())).clone();
            randomShape.setShapeColor(m_colors.get(rand.nextInt(m_colors.size())));
            order[i] = randomShape;
        }
        return order;
    }
      
    /**
    * HashCode redefinition
    * @return hashCode
    */
    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), m_colors, m_displayTime, 
                m_intervalDuration, m_numberOfAppearance, m_shapes);
    }

    /**
    * Tells if the object passed in parameters is equal to this one
    * @param obj
    *   the object to compare to
    * @Return if they are equals
    */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final VisualDistractorParameter visualParam = (VisualDistractorParameter)obj;
        
        return super.equals(visualParam) 
                && Objects.equals(m_colors, visualParam.m_colors)
                && Objects.equals(m_displayTime, visualParam.m_displayTime)
                && Objects.equals(m_intervalDuration, visualParam.m_intervalDuration)
                && Objects.equals(m_numberOfAppearance, visualParam.m_numberOfAppearance)
                && Objects.equals(m_shapes, visualParam.m_shapes);        
    } 
}
