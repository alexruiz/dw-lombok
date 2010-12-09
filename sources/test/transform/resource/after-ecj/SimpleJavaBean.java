import lombok.AccessLevel;
import com.developerworks.lombok.GenerateJavaBean;
import com.developerworks.lombok.GenerateBoundSetter;
@GenerateJavaBean class SimpleJavaBean {
  @GenerateBoundSetter int i;
  @GenerateBoundSetter(AccessLevel.PUBLIC) String s;
  @GenerateBoundSetter(AccessLevel.PROTECTED) float f;
  @GenerateBoundSetter(AccessLevel.PACKAGE) Object o;
  @GenerateBoundSetter(AccessLevel.PRIVATE) double d;
  public static final java.lang.String PROP_I = new java.lang.String("i");
  public static final java.lang.String PROP_S = new java.lang.String("s");
  public static final java.lang.String PROP_F = new java.lang.String("f");
  public static final java.lang.String PROP_O = new java.lang.String("o");
  public static final java.lang.String PROP_D = new java.lang.String("d");
  private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
  <clinit>() {
  }
  public @java.lang.SuppressWarnings("all") void setI(int i) {
    final int old = this.i;
    this.i = i;
    propertySupport.firePropertyChange(PROP_I, old, this.i);
  }
  public @java.lang.SuppressWarnings("all") void setS(String s) {
    final String old = this.s;
    this.s = s;
    propertySupport.firePropertyChange(PROP_S, old, this.s);
  }
  protected @java.lang.SuppressWarnings("all") void setF(float f) {
    final float old = this.f;
    this.f = f;
    propertySupport.firePropertyChange(PROP_F, old, this.f);
  }
  @java.lang.SuppressWarnings("all") void setO(Object o) {
    final Object old = this.o;
    this.o = o;
    propertySupport.firePropertyChange(PROP_O, old, this.o);
  }
  private @java.lang.SuppressWarnings("all") void setD(double d) {
    final double old = this.d;
    this.d = d;
    propertySupport.firePropertyChange(PROP_D, old, this.d);
  }
  public @java.lang.SuppressWarnings("all") void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.addPropertyChangeListener(listener);
  }
  public @java.lang.SuppressWarnings("all") void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.removePropertyChangeListener(listener);
  }
  SimpleJavaBean() {
    super();
  }
}