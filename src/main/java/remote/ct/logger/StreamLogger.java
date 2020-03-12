package remote.ct.logger;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorator over OutputStream
 * 
 * @author Kaliutau_AV
 */
public class StreamLogger extends OutputStream {

	// The default logger
	private static final Logger LOG = LoggerFactory.getLogger(StreamLogger.class);

	private final StringBuilder stringBuilder = new StringBuilder();

	/**
	 * Save or redirect output This implementation simply redirects output to system
	 * Logger without saving
	 */
	@Override
	public void write(int b) throws IOException {
		char current = (char) b;
		if (current == '\n') {
			LOG.info(stringBuilder.toString());
			//stringBuilder.setLength(0);
		} else {
			stringBuilder.append(current);
		}

	}
	
	public String getLog() {
		String out = stringBuilder.toString();
		stringBuilder.setLength(0);
		return out;
	}

}
