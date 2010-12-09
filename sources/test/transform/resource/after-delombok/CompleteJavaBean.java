class CompleteJavaBean {
	
	private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
	public static final java.lang.String PROP_I = "i";
	private int i;
	
	public void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
	
	@java.lang.SuppressWarnings("all")
	public void setI(int i) {
		final int old = this.i;
		this.i = i;
		propertySupport.firePropertyChange(PROP_I, old, this.i);
	}
}
