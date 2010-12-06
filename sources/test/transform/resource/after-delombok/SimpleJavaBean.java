class SetterOnMethod {
	
	int i;
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
}
