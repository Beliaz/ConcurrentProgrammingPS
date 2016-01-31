package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller
{
    @FXML
    private Button cmdDownload;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField txtUrl;

    @FXML
    private Label lblFileInfo;

    @FXML
    private Label lblError;

    private Thread mDownloadThread;
    private Thread mInfoThread;

    @FXML
    private void initialize()
    {
        mInfoThread = null;
        mDownloadThread = null;

        txtUrl.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(!newValue.isEmpty())
                checkUrl(newValue);
        });
    }

    @FXML protected void onClose(ActionEvent event)
    {
        if (mDownloadThread != null)
            mDownloadThread.interrupt();

        if (mInfoThread != null)
            mInfoThread.interrupt();

        Platform.exit();
    }

    @FXML protected void onDownloadAction(ActionEvent event)
    {
        if(txtUrl.getText().isEmpty())
            return;

        if (cmdDownload.getText().equals("Download"))
        {
            progressBar.setVisible(true);
            cmdDownload.setText("Cancel");

            String url = txtUrl.getText();
            String filename = url.substring( url.lastIndexOf('/')+1, url.length() );

            Task downloadTask = createDownloadTask(url,filename);

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);

            progressBar.progressProperty().bind(downloadTask.progressProperty());

            mDownloadThread = new Thread(downloadTask);
            mDownloadThread.start();
        }
        else
        {
            cmdDownload.setText("Download");
            mDownloadThread.interrupt();
        }
    }

    public Task createDownloadTask(final String url, final String destination)
    {
        return new Task() {
            @Override
            protected Object call() throws Exception
            {
                updateProgress(0.0,1.0);

                try
                {
                    HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url).openConnection());
                    long completeFileSize = httpConnection.getContentLength();


                    java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(destination);



                    BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0 && !Thread.currentThread().isInterrupted())
                    {
                        downloadedFileSize += x;

                        updateProgress(downloadedFileSize,completeFileSize);

                        bout.write(data, 0, x);
                    }

                    bout.close();
                    in.close();


                    if (Thread.currentThread().isInterrupted())
                    {
                        new File(destination).delete();

                        Platform.runLater(() -> {
                            lblError.setText("");
                            cmdDownload.setText("Download");

                            updateProgress(0,0);
                            progressBar.setVisible(false);
                        });
                    }
                    else
                    {
                        Platform.runLater(() -> {
                            txtUrl.setText("");
                            lblFileInfo.setText("SAVED");
                            lblError.setText("");

                            cmdDownload.setText("Download");

                            updateProgress(0,0);
                            progressBar.setVisible(false);
                        });
                    }
                }
                catch (FileNotFoundException e)
                {
                    Platform.runLater(() -> {
                        lblFileInfo.setText("");
                        lblError.setText("File not found");
                    });
                }
                catch (IOException e)
                {
                    Platform.runLater(() -> {
                        lblFileInfo.setText("");
                        lblError.setText("Error downloading file");
                    });
                }


                return true;
            }
        };
    }

    @FXML protected void onUrlChange(ActionEvent event) {
        System.out.println(txtUrl.getText());
    }

    void checkUrl(final String url)
    {
        if (mInfoThread != null && mInfoThread.getState() != Thread.State.TERMINATED)
        {
            mInfoThread.interrupt();
        }

        // separate non-FX thread
        mInfoThread = new Thread() {

            // runnable for that thread
            public void run()
            {
                HttpURLConnection conn = null;
                try
                {
                    conn = (HttpURLConnection)new URL(url).openConnection();
                    conn.setRequestMethod("HEAD");
                    conn.getInputStream();

                    double len = (double)conn.getContentLength();
                    String ext = "byte";

                    if (len < 0)
                    {
                        if(conn != null)
                            conn.disconnect();

                        Platform.runLater(() -> {
                            lblFileInfo.setText("");
                            lblError.setText("Invalid url");
                        });

                        return;
                    }

                    if(len > 1000)
                    {
                        len /= 1000.0;
                        ext = "kb";
                    }
                    if(len > 1000)
                    {
                        len /= 1000.0;
                        ext = "mb";
                    }
                    if(len > 1000)
                    {
                        len /= 1000.0;
                        ext = "gb";
                    }

                    String filename = url.substring( url.lastIndexOf('/')+1, url.length() );

                    final String infoStr =  "Filename: " + filename + "\n" +
                                            "Filesize: "+String.format( "%.2f", len)+ ext;

                    if (isInterrupted())
                        return;

                    Platform.runLater(() -> {
                        lblFileInfo.setText(infoStr);
                        lblError.setText("");
                    });
                }
                catch (MalformedURLException e)
                {
                    if (isInterrupted())
                        return;

                    Platform.runLater(() -> {
                        lblFileInfo.setText("");
                        lblError.setText("Invalid url");
                    });

                    return;
                }
                catch (Exception e)
                {
                    if (isInterrupted())
                        return;

                    Platform.runLater(() -> {
                        lblFileInfo.setText("");
                        lblError.setText("Invalid url");
                    });

                    return;
                }
                finally
                {
                    if(conn != null)
                        conn.disconnect();
                }
            }
        };
        mInfoThread.start();
    }
}
