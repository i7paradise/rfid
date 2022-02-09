/**
 * 
 */
package uhf_test_2;

import java.io.Serializable;

/**
 * @author hmr
 *
 */
public class RFIDTag implements Serializable {
	public String _ReaderName = "";

	public String _PC = null;

	public String _EPC = null;

	public String _TagType = "6C";

	public int _ANT_NUM = 0;
}
