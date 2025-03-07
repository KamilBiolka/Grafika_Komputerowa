import pygame
import math

pygame.init()
screen = pygame.display.set_mode((800, 600))
clock = pygame.time.Clock()

WHITE = (255, 255, 255)
YELLOW = (255, 255, 0)
BLUE = (200, 150, 200)
BLACK = (0, 0, 0)

def get_pentagon_vertices(center, radius):
    cx, cy = center
    vertices = []
    for i in range(5):
        angle = math.radians(72 * i - 90)  
        x = cx + radius * math.cos(angle)
        y = cy + radius * math.sin(angle)
        vertices.append((x, y))
    return vertices


def rotate_polygon(vertices, angle, center):
    cx, cy = center
    angle = math.radians(angle)
    rotated = []
    for x, y in vertices:
        x_new = cx + (x - cx) * math.cos(angle) - (y - cy) * math.sin(angle)
        y_new = cy + (x - cx) * math.sin(angle) + (y - cy) * math.cos(angle)
        rotated.append((x_new, y_new))
    return rotated


def scale_polygon(vertices, scale_x, scale_y, center):
    cx, cy = center
    scaled = []
    for x, y in vertices:
        x_new = cx + (x - cx) * scale_x
        y_new = cy + (y - cy) * scale_y
        scaled.append((x_new, y_new))
    return scaled


def transform_to_parallelogram(vertices, center, shear_x=0.4, shear_y=0.2, scale_x=1.2, scale_y=0.8):
    transformed = []
    cx, cy = center
    avg_x = sum(x for x, y in vertices) / len(vertices)
    avg_y = sum(y for x, y in vertices) / len(vertices)
    
    for x, y in vertices:
        x -= avg_x
        y -= avg_y
        x_new = x + shear_x * y  
        y_new = y + shear_y * x  
        x_new *= scale_x  
        y_new *= scale_y  
        transformed.append((x_new + cx, y_new + cy))
    
    return transformed


center = (400, 300)
radius = 100
original_vertices = get_pentagon_vertices(center, radius)
vertices = original_vertices[:]

running = True
while running:
    screen.fill(YELLOW)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_1:
                vertices = scale_polygon(original_vertices, 0.7, 0.7, center)
            elif event.key == pygame.K_2:
                vertices = rotate_polygon(original_vertices, 20, center)
            elif event.key == pygame.K_3:
                vertices = scale_polygon(original_vertices, 0.6, 1.5, center)
            elif event.key == pygame.K_4:
                vertices = transform_to_parallelogram(original_vertices, center)
            elif event.key == pygame.K_5:
                new_center = (400, 100)
                vertices = get_pentagon_vertices(new_center, 80)
            elif event.key == pygame.K_6:
                new_center = (400, 300)
                vertices = transform_to_parallelogram(original_vertices, new_center, shear_x=0.4, shear_y=0.2, scale_x=0.8, scale_y=1.5)
            elif event.key == pygame.K_7:
                vertices = scale_polygon(original_vertices, 0.6, 1.5, center)
            elif event.key == pygame.K_8:
                new_center = (200, 500)
                vertices = scale_polygon(get_pentagon_vertices(new_center, 90), 1.5, 0.7, new_center)
                vertices = rotate_polygon(vertices, -20, new_center)
            elif event.key == pygame.K_9:
                new_center = (650, 350)
                vertices = transform_to_parallelogram(original_vertices, new_center, shear_x=0.4, shear_y=0.2, scale_x=1.2, scale_y=0.8)
                vertices = rotate_polygon(vertices, 180, new_center)

    pygame.draw.polygon(screen, BLUE, vertices)
    pygame.draw.polygon(screen, BLACK, vertices, 2)
    pygame.display.flip()
    clock.tick(30)

pygame.quit()