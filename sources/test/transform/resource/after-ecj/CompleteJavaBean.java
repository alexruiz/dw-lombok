import lombok.GenerateJavaBean;
import lombok.GenerateBoundSetter;
@GenerateJavaBean class CompleteJavaBean {
  private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
  public static final java.lang.String PROP_I = "i";
  private @GenerateBoundSetter int i;
  <clinit>() {
  }
  public @java.lang.SuppressWarnings("all") void setI(int i) {
    final int old = this.i;
    this.i = i;
    propertySupport.firePropertyChange(PROP_I, old, this.i);
  }
  public @java.lang.SuppressWarnings("all") void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.addPropertyChangeListener(listener);
  }
  public @java.lang.SuppressWarnings("all") void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.removePropertyChangeListener(listener);
  }
  CompleteJavaBean() {
    super();
  }
  public void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.addPropertyChangeListener(listener);
  }
  public void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.removePropertyChangeListener(listener);
  }
}