import pygame


pygame.init()

WIDTH, HEIGHT = 400, 400
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Rysowanie figury")

BLACK = (0, 0, 0)
YELLOW = (255, 255, 0)

circle_center = (WIDTH // 2, HEIGHT // 2)
circle_radius = 100
square_size = 100
square_pos = (circle_center[0] - square_size // 2, circle_center[1] - square_size // 2)

running = True
while running:
    screen.fill((255, 255, 255))  
    

    pygame.draw.circle(screen, BLACK, circle_center, circle_radius)
    
    pygame.draw.rect(screen, YELLOW, (*square_pos, square_size, square_size))
    
    pygame.display.flip()
    
    
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

pygame.quit()
