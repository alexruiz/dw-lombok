import lombok.GenerateJavaBean;
import lombok.GenerateBoundSetter;

@GenerateJavaBean
class CompleteJavaBean {

  private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
  
  public static final java.lang.String PROP_I = "i";
  @GenerateBoundSetter private int i;
  
  public void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.removePropertyChangeListener(listener);
  }
}
