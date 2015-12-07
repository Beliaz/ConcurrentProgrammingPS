package uibk.ac.at.Task1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by flori on 06.12.2015.
 */
public class MyEvent implements Event {
    private final long creatorThreadId;
    private final Date creationDate;

    MyEvent()
    {
        creatorThreadId = Thread.currentThread().getId();
        creationDate = Calendar.getInstance().getTime();
    }

    public long getCreatorThreadId() { return creatorThreadId; }
    public Date getCreationDate() { return new Date(creationDate.getTime()); }

    @Override
    public String toString() {
        return String.format("MyEvent(%s, %d)",
                new SimpleDateFormat("HH:mm:ss").format(creationDate), creatorThreadId);
    }
}
