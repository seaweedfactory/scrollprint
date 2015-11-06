package scrollPrint;

import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.io.*;
import java.util.Enumeration;
import javax.swing.JFrame;

//Mostly from http://playground.arduino.cc/Interfacing/Java example code
public class ScrollPrinter implements SerialPortEventListener 
{
        private SerialPort serialPort;
        private File file;
        private BufferedReader reader;
        public static int INITIAL_DELAY_DEFAULT = 1500;
        public static int LINE_DELAY_DEFAULT = 30;
                
        public ScrollPrinter()
        {
            reader = null;
            file = null;
            serialPort = null;
        }
        
        public void printScroll(String filename, int initialDelay, int lineDelay) throws Exception 
        {
            try 
            {
                BufferedReader reader = null;
                this.initialize();
                file = new File(filename);
                reader = new BufferedReader(new FileReader(file));
                String textLine = null;

                //Initial delay allows connection and printer to settle
                //without this, characters are lost and alignment is off
                Thread.sleep(initialDelay);

                // repeat until all lines is read
                while ((textLine = reader.readLine()) != null) 
                {
                    //read each line a chracter at a time to the printer
                    for(int i=0; i < 32; i++)
                    {
                        char bit = textLine.charAt(i);
                        this.output.write(bit);
                        
                        //Sleep after each character to prevent buffer overflow
                        Thread.sleep(lineDelay);
                    }
                } 
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
                throw e;
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
                throw e;
            }
            finally 
            {
                try 
                {
                    if (reader != null) 
                    {
                        reader.close();
                    }
                    
                    this.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        
        
        
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
			};
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() throws Exception
        {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) 
                {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) 
                        {
				if (currPortId.getName().equals(portName)) 
                                {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) 
                {
			System.out.println("Could not find COM port.");
                        throw new Exception("Could not find COM port.");
		}

		try 
                {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} 
                catch (Exception e) 
                {
			System.err.println(e.toString());
                        throw e;
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() 
        {
		if (serialPort != null) 
                {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent)
        {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
                {
			try 
                        {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				// Displayed results are codepage dependent
				System.out.print(new String(chunk));
			} 
                        catch (Exception e) 
                        {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

}
