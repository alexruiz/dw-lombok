class SimpleJavaBean {
	
	int i;
	String s;
	float f;
	Object o;
	double d;
	private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
	
	@java.lang.SuppressWarnings("all")
	public void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}
	
	@java.lang.SuppressWarnings("all")
	public void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
	public static final java.lang.String PROP_I = new java.lang.String("i");
	
	@java.lang.SuppressWarnings("all")
	public void setI(int i) {
		final int old = this.i;
		this.i = i;
		propertySupport.firePropertyChange(PROP_I, old, this.i);
	}
	public static final java.lang.String PROP_S = new java.lang.String("s");
	
	@java.lang.SuppressWarnings("all")
	public void setS(String s) {
		final String old = this.s;
		this.s = s;
		propertySupport.firePropertyChange(PROP_S, old, this.s);
	}
	public static final java.lang.String PROP_F = new java.lang.String("f");
	
	@java.lang.SuppressWarnings("all")
	protected void setF(float f) {
		final float old = this.f;
		this.f = f;
		propertySupport.firePropertyChange(PROP_F, old, this.f);
	}
	public static final java.lang.String PROP_O = new java.lang.String("o");
	
	@java.lang.SuppressWarnings("all")
	void setO(Object o) {
		final Object old = this.o;
		this.o = o;
		propertySupport.firePropertyChange(PROP_O, old, this.o);
	}
	public static final java.lang.String PROP_D = new java.lang.String("d");
	
	@java.lang.SuppressWarnings("all")
	private void setD(double d) {
		final double old = this.d;
		this.d = d;
		propertySupport.firePropertyChange(PROP_D, old, this.d);
	}
}
