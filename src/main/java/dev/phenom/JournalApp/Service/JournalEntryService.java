package dev.phenom.JournalApp.Service;

import dev.phenom.JournalApp.Entity.JournalEntry;
import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry entry, String userName){
        User user = userService.getByUserName(userName);
        entry.setDate(LocalDateTime.now());
        JournalEntry save = journalEntryRepository.save(entry);
        user.getJournalEntryList().add(save);
        userService.saveUser(user);
    }
    public void saveEntry(JournalEntry entry){
        entry.setDate(LocalDateTime.now());
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findbyId(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public boolean deleteEntryById(ObjectId id, String userName) throws RuntimeException {
        boolean removed=false;
        try {
            User user = userService.getByUserName(userName);
            removed = user.getJournalEntryList().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("An Error Occur while deleting the Entry.",e);
        }
        return removed;
    }

//    public List<JournalEntry> findByUserName(String userName) {}
}
