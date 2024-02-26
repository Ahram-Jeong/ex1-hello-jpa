package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ash"); // 애플리케이션 로딩 시점에 딱 하나만 생성 (web server가 올라오는 시점에 db당 하나만 생성)
        EntityManager em = emf.createEntityManager(); // 트랜잭션 단위 별로 생성 (클라이언트 요청이 올 때마다 생성, 사용하고 바로 close)

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            Member findMember = em.find(Member.class, 1L);
            List<Member> findMembers = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(0) // 페이징 처리
                    .setMaxResults(10) // 페이징 처리
                    .getResultList();

            for (Member findMember : findMembers) {
                System.out.println("findMember = " + findMember.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
