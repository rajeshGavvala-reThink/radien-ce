package io.radien.ms.rolemanagement.client.entities;

import io.radien.ms.rolemanagement.client.services.RoleFactory;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

public class RoleTest extends TestCase {

    private Role role;

    Date now = new Date();

    public RoleTest() {
        role = RoleFactory.create("name", "description", 3L);
        role.setId(2L);
        role.setTerminationDate(now);
    }


    @Test
    public void testConstructor() {
        Role newRole = new Role(role);
        assertEquals((Long) 2L, newRole.getId());
        assertEquals("name", newRole.getName());
        assertEquals("description", newRole.getDescription());
        assertEquals(now, newRole.getTerminationDate());
    }

    @Test
    public void testGetId() {
        assertNotNull(role.getId());
        assertEquals((Long) 2L, role.getId());
    }

    @Test
    public void testSetId() {
        role.setId(3L);
        assertNotNull(role.getId());
        assertEquals((Long) 3L, role.getId());
    }

    @Test
    public void testGetName() {
        assertNotNull(role.getName());
        assertEquals("name", role.getName());
    }

    @Test
    public void testSetName() {
        role.setName("newName");
        assertNotNull(role.getName());
        assertEquals("newName", role.getName());
    }

    @Test
    public void testGetDescription() {
        assertNotNull(role.getDescription());
        assertEquals("description", role.getDescription());
    }

    @Test
    public void testSetDescription() {
        role.setDescription("newDescription");
        assertNotNull(role.getDescription());
        assertEquals("newDescription", role.getDescription());
    }

    @Test
    public void testGetTerminationDate() {
        assertNotNull(role.getTerminationDate());
        assertEquals(now, role.getTerminationDate());
    }

    @Test
    public void testSetTerminationDate() {
        Date newDate = new Date();
        role.setTerminationDate(newDate);
        assertNotNull(role.getTerminationDate());
        assertEquals(newDate, role.getTerminationDate());
    }
}
